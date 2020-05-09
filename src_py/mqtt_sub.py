import paho.mqtt.client as mqtt
import pymysql.cursors
from pymysqlpool.pool import Pool
import time, json

HOST = "iotplatform.xyz"
TOPIC = "sensor/humidity"
SQL = "INSERT INTO `humidity` (`device`, `timestamp`, `state`, `value`) VALUES (%s, %s, %s, %s)"

def extract_data(data:dict) -> tuple:
    device_id = data['device_id']
    pair = data['value']
    state = int(pair[0])
    value = int(pair[1])
    return device_id, state, value

def on_connect(client, userdata, flags, rc):
    if rc == 0:
        print("[INFO] Connected to broker")
        global Connected
        Connected = True
    else:
        print("[ERROR] Connection failed")
 
def on_message(client, userdata, message:mqtt.MQTTMessage):
    data = json.loads(message.payload.decode("utf-8"))

    try:
        device_id, state, value = extract_data(data)
    except:
        print("[ERROR] JSON format is wrong.")
        return

    connection = pool.get_conn()
    try:
        with connection.cursor() as cursor:
            # Create a new record
            timestamp = int(time.time()) 
            cursor.execute(SQL, (device_id, timestamp, state, value))
    except:
        print("[ERROR] Database error.")
    finally:
        pool.release(connection)

    print("[INFO] %s, %s"%(device_id, time.ctime(timestamp)))

if __name__ == "__main__":
    
    pool = Pool(host=HOST, port=3306, user='root', password='a123', 
        db='iotplatform', cursorclass=pymysql.cursors.DictCursor,
        autocommit=True, min_size=1, max_size=10)
    pool.init()

    Connected = False
 
    client = mqtt.Client()
    client.on_connect = on_connect
    client.on_message = on_message
    
    client.connect(HOST)
    client.loop_start()
    
    while Connected != True:
        time.sleep(0.1)
    
    client.subscribe(TOPIC)
    
    try:
        while True:
            time.sleep(1)
    
    except KeyboardInterrupt:
        client.disconnect()
        client.loop_stop()
        pool.destroy()
 
