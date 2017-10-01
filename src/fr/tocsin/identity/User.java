package fr.tocsin.identity;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName = "Users")
public class User {

    private String key;
    private String channelId;

    @DynamoDBHashKey(attributeName = "Key")
    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }

    @DynamoDBAttribute(attributeName = "ChannelID")
    public String getChannelId() {
        return channelId;
    }
    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

}
