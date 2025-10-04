**Description**: instructions-capture-service is a REST API created to consume JSON messages from kafka topic 
convert it in canonical format
apply transformation(masking / validation)
then produce a kafka message in platform specific JSON to outbound topic.

**Steps to execute the API**
1. Start zookeeper and Kafka server
2. Create topics:
kafka-topics.sh --create --topic instructions.inbound --bootstrap-server localhost:9092 --partitions 1 --replication-factor 1
kafka-topics.sh --create --topic instructions.outbound --bootstrap-server localhost:9092 --partitions 1 --replication-factor 1 
3. Run Spring Boot app. 
4. Produce a test JSON message:
kafka-console-producer.sh --topic instructions.inbound --bootstrap-server localhost:9092
> {
"platformid": "ACCT123",
"acctnumber" : "acct1234",
"secid" : "abc123",
"type":"Buy",
"amount":10000,
"time":"2025-10-02T00:00:00z"
} 

5. Call API:
curl http://localhost:8080/instructions/process-messages

6. Check output-topic:
kafka-console-consumer.sh --topic instructions.outbound --bootstrap-server localhost:9092 --from-beginning

******************
**To test API without kafka** - Run "[TradeControllerFileTest.java](src/test/java/com/example/instructions/controller/TradeControllerFileTest.java)"
which reads "input.json" as a message & process it & print converted platformTrade JSON.
