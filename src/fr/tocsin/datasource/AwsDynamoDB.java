package fr.tocsin.datasource;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.TableCollection;
import com.amazonaws.services.dynamodbv2.model.*;
import fr.tocsin.Properties;
import fr.tocsin.stock.IndicatorValue;

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

    public static void createTable(String tableName) {

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
                System.err.println("CreateTable request failed for " + tableName);
                System.err.println(e.getMessage());
            }
        }
    }

    public static void getTableInformation(String tableName) {

        System.out.println("Describing " + tableName);

        TableDescription tableDescription = dynamoDB.getTable(tableName).describe();
        System.out.format(
                "Name: %s:\n" + "Status: %s \n" + "Provisioned Throughput (read capacity units/sec): %d \n"
                        + "Provisioned Throughput (write capacity units/sec): %d \n",
                tableDescription.getTableName(), tableDescription.getTableStatus(),
                tableDescription.getProvisionedThroughput().getReadCapacityUnits(),
                tableDescription.getProvisionedThroughput().getWriteCapacityUnits());
    }

    public static boolean hasTable(String tableName) {

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

    public static void setIndicatorValue(IndicatorValue indicatorValue) {
        mapper.save(indicatorValue);
    }

    public static void getIndicatorValue(String tableName, String key) {
        Table table = dynamoDB.getTable(tableName);

        try {
            Item item = table.getItem("Key", key);

            System.out.println("Printing item after retrieving it....");
            System.out.println(item.toJSONPretty());

        } catch (Exception e) {
            System.err.println("GetItem failed.");
            System.err.println(e.getMessage());
        }
    }

}

/* Useful CLI commands:

        aws dynamodb scan --table-name IndicatorValues --endpoint-url http://localhost:8000

*/