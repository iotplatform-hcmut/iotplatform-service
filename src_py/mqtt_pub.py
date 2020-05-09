import paho.mqtt.client as mqtt
import time, random as rd

# IP address (Host)
HOST = "iotplatform.xyz"
# Topic name
topic = "sensor/humidity"

client = mqtt.Client()
client.connect(HOST)

while(True):
    pattern = '{"device_id":"d7_1","value":["%s","%s"]}'
    data = pattern%(rd.randint(0,1), rd.randint(0,1023))
    client.publish(topic, data)
    time.sleep(1)

client.disconnect()