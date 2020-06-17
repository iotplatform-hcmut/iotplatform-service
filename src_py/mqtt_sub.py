import paho.mqtt.client as mqtt
import pymysql.cursors
from pymysqlpool.pool import Pool
import time, json

HOST = "iotplatform.xyz"
TOPIC = "sensor/humidity"
SQL = "INSERT INTO `humidity` (`id`, `timestamp`, `value`) VALUES (%s, %s, %s)"

def extract_data(data:list):
    result = []
    for ele in data:
        id = ele['device_id']
        pair = ele['values']
        value = int(pair[0])
        result += [[id, value]]
    return result

def on_connect(client, userdata, flags, rc):
    if rc == 0:
        print("[INFO] Connected to broker")
        global Connected
        Connected = True
    else:
        print("[ERROR] Connection failed")
 
def on_message(client, userdata, message:mqtt.MQTTMessage):
    data = json.loads(message.payload.decode("utf-8"))
    print(data)
    try:
        arrData = extract_data(data)
        print(arrData)
    except:
        print("[ERROR] JSON format is wrong.")
        return

    connection = pool.get_conn()
    try:
        with connection.cursor() as cursor:
            # Create a new record
            timestamp = int(time.time()) 
            for data in arrData:
                cursor.execute(SQL, (data[0], timestamp, data[1]))
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
    client.username_pw_set("BKvm2","Hcmut_CSE_2020")

    print("------------------\n")
    

    client.connect("13.76.250.158")
    client.loop_start()
    
    while Connected != True:
        time.sleep(0.1)
    
    client.subscribe("Topic/Mois")
    
    try:
        while True:
            time.sleep(1)
    
    except (KeyboardInterrupt, SystemExit):
        client.disconnect()
        client.loop_stop()
        pool.destroy()
        print("unSub")
 
