instructions-capture-service is a REST API created to consume JSON messages from kafka topic 
convert it in canonical format
apply transformation(masking / validation)
then produce a kafka message in platform specific JSON to outbound topic.
