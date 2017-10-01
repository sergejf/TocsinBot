// FIXME: refactor methods to start with db (e.g. dbGetX or dbGet) to be less confusing with normal getters/setters
// TODO: implement dbScanPositions, http://docs.aws.amazon.com/amazondynamodb/latest/developerguide/GettingStarted.Java.04.html
// TODO: move DynamoDB from local to AWS, http://docs.aws.amazon.com/amazondynamodb/latest/developerguide/SettingUp.DynamoWebService.html

package fr.tocsin.datasource;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.TableCollection;
import com.amazonaws.services.dynamodbv2.model.*;
import fr.tocsin.Properties;
import fr.tocsin.identity.User;
import fr.tocsin.stock.Bar;
import fr.tocsin.stock.IndicatorValue;
import fr.tocsin.stock.Portfolio;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AwsDynamoDB {

    private static AwsDynamoDB awsDB = null;
    private static DynamoDB dynamoDB;
    private static DynamoDBMapper mapper;

    public static AwsDynamoDB getDB() {

        if (awsDB == null) {
            Properties cfg = new Properties();
            AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().withEndpointConfiguration(
                    new AwsClientBuilder.EndpointConfiguration(cfg.getProperty("db.local"), "us-west-2")).build();
            dynamoDB = new DynamoDB(client);
            mapper = new DynamoDBMapper(client);
            awsDB = new AwsDynamoDB();
        }
        return awsDB;
    }

    public void createTable(String tableName) {

        if (!hasTable(tableName)) {
            try {

                // Set up primary key
                List<AttributeDefinition> attributeDefinitions = new ArrayList<>();
                attributeDefinitions.add(new AttributeDefinition().withAttributeName("Key").withAttributeType("S"));
                List<KeySchemaElement> keySchema = new ArrayList<>();
                keySchema.add(new KeySchemaElement().withAttributeName("Key").withKeyType(KeyType.HASH));

                // Create table request setup (capacity based on free tiers)
                CreateTableRequest request = new CreateTableRequest().withTableName(tableName).withKeySchema(keySchema)
                        .withAttributeDefinitions(attributeDefinitions).withProvisionedThroughput(
                                new ProvisionedThroughput().withReadCapacityUnits(25L).withWriteCapacityUnits(25L));

                // Creating table
                System.out.println("Issuing CreateTable request for " + tableName);
                Table table = dynamoDB.createTable(request);

                System.out.println("Waiting for " + tableName + " to be created...this may take a while...");
                table.waitForActive();

                // Output table information
                getTableInformation(tableName);
            } catch (Exception e) {
                System.err.println("createTable failed. Table name: " + tableName);
                System.err.println(e.getMessage());
            }
        }
    }

    public void getTableInformation(String tableName) {

        System.out.println("Describing " + tableName);

        TableDescription tableDescription = dynamoDB.getTable(tableName).describe();
        System.out.format(
                "Name: %s:\n" + "Status: %s \n" + "Provisioned Throughput (read capacity units/sec): %d \n"
                        + "Provisioned Throughput (write capacity units/sec): %d \n",
                tableDescription.getTableName(), tableDescription.getTableStatus(),
                tableDescription.getProvisionedThroughput().getReadCapacityUnits(),
                tableDescription.getProvisionedThroughput().getWriteCapacityUnits());
    }

    public boolean hasTable(String tableName) {

        TableCollection<ListTablesResult> tables = dynamoDB.listTables();
        Iterator<Table> iterator = tables.iterator();

        while (iterator.hasNext()) {
            Table table = iterator.next();
            if (tableName.equals(table.getTableName())) {
                return true;
            }
        }
        return false;
    }

    public void setIndicatorValue(IndicatorValue indicatorValue) {
        mapper.save(indicatorValue);
    }

    // Key format: "2017-09-29MORLWILLR14"
    public IndicatorValue getIndicatorValue(String key) {
        try {
            IndicatorValue indicatorValue = mapper.load(IndicatorValue.class, key);
            return indicatorValue;

        } catch (Exception e) {
            System.err.println("getIndicatorValue failed.");
            System.err.println(e.getMessage());
        }
        return null;
    }

    public void setBar(Bar bar) {
        mapper.save(bar);
    }

    // Key format "2017-09-29MORL"
    public Bar getBar(String key) {
        try {
            Bar bar = mapper.load(Bar.class, key);
            return bar;

        } catch (Exception e) {
            System.err.println("getBar failed.");
            System.err.println(e.getMessage());
        }
        return null;
    }

    public void setUser(User user) {
        mapper.save(user);
    }

    // Key format "messenger:123456789"
    public User getUser(String key) {
        try {
            User user = mapper.load(User.class, key);
            return user;

        } catch (Exception e) {
            System.err.println("getUser failed.");
            System.err.println(e.getMessage());
        }
        return null;
    }

    public void setPortfolio(Portfolio portfolio) {
        mapper.save(portfolio);
    }

    // Key format "PortfolioNamemessenger:123456789"
    public Portfolio getPortfolio(String key) {
        try {
            Portfolio portfolio = mapper.load(Portfolio.class, key);
            return portfolio;

        } catch (Exception e) {
            System.err.println("getPortfolio failed.");
            System.err.println(e.getMessage());
        }
        return null;
    }

}

/* Useful CLI commands:

        aws dynamodb scan --table-name IndicatorValues --endpoint-url http://localhost:8000
        aws dynamodb scan --table-name Bars --endpoint-url http://localhost:8000
        aws dynamodb scan --table-name Users --endpoint-url http://localhost:8000

*/