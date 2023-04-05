import json
import random
import time
import pandas as pd

final_data = {}
random.seed(913)

# Allocation
data = {}
for i in range(100):
    temp = {}
    temp['aid'] = i + 1
    temp['n_police'] = random.randint(0, 10)
    temp['n_ambulance'] = random.randint(0, 5)
    temp['n_truck'] = random.randint(0, 5)
    temp['n_train'] = random.randint(0, 3)
    temp['n_bus'] = random.randint(0, 10)
    data['Allocation' + str(i + 1)] = temp
final_data['Allocation'] = data


# Bus
def bus_number():
    num_set = [chr(i) for i in range(48, 58)]
    first = str(random.randint(13, 23))
    second = '-D-'
    third = ''.join(random.choices(num_set, k=5))
    return first + second + third


stop_list = ['Parnell Square Area', 'O\'Connell Street Area', 'The Quays',
             'Trinity College Area', 'Dame Street Area', 'Nassau Street Area',
             'St. Stephen\'s Green Area']
data = {}
for i in range(100):
    temp = {}
    temp['bid'] = i + 1
    temp['bus_number'] = bus_number()
    temp['name'] = stop_list[random.randint(0, 6)]
    temp['bus_type'] = random.randint(0, 1)
    temp['bus_available'] = random.randint(0, 1)
    temp['latitude'] = 53.35 + random.random() % 0.03
    temp['longitude'] = -6.26 + random.random() % 0.03
    data['Bus' + str(i + 1)] = temp
final_data['Bus'] = data

# DisasterInfo
disaster_type = ["fire", "water", "other"]
radius = [5, 10, 20, 50, 100, 200]
injure = [5, 10, 12, 50, 80, 100, 210]


def location_generate():
    letter_set = [chr(i) for i in range(65, 91)]
    letter_set.extend([chr(i) for i in range(97, 123)])
    length = random.randint(4, 16)
    return ''.join(random.choices(letter_set, k=length))


def otime_generate():
    random_time = time.time() - random.randint(0, 86400) * 60
    return int(random_time) * 1000

def description_generate():
    letter_set = [chr(i) for i in range(65, 91)]
    letter_set.extend([chr(i) for i in range(97, 123)])
    length = random.randint(4, 30)
    return ''.join(random.choices(letter_set, k=length))


data = {}
for i in range(500):
    temp = {}
    temp['did'] = i + 1
    temp['location'] = location_generate()
    temp['radius'] = radius[random.randint(0,5)]
    temp['disasterType'] = disaster_type[random.randint(0,2)]
    temp['otime'] = otime_generate()
    temp['latitude'] = 53.35 + random.random()%0.03
    temp['longitude'] = -6.26 + random.random()%0.03
    temp['injury'] = random.randint(1,20)
    data['DisasterInfo' + str(i + 1)] = temp

temp = {}
temp['did'] = i + 1
temp['location'] = "Trinity College Dublin"
temp['radius'] = 100
temp['disasterType'] = disaster_type[random.randint(0,2)]
temp['otime'] = int(time.time()) * 1000
temp['latitude'] = 53.3442016
temp['longitude'] = -6.2544264
temp['injury'] = random.randint(1,20)
data['DisasterInfo' + str(i + 1)] = temp

final_data['DisasterInfo'] = data


# Firebrigade
fire_list = ['Dublin Fire Brigade Hq', 'Donnybrook Fire Station']

data = {}
for i in range(2):
    temp = {}
    temp['fid'] = i + 1
    temp['name'] = fire_list[i]
    temp['n_firefighter'] = random.randint(1, 5) * 10
    temp['n_truck'] = random.randint(3, 10)
    temp['n_ava_firefighter'] = max(temp['n_firefighter'] - random.randint(1, 5) * 5, 0)
    temp['n_ava_truck'] = max(temp['n_truck'] - random.randint(3, 10), 0)
    temp['latitude'] = 53.35 + random.random() % 0.03
    temp['longitude'] = -6.26 + random.random() % 0.03
    data['FireBrigade' + str(i + 1)] = temp
final_data['FireBrigade'] = data

# Garda
garda_list = ['Bridewell', 'Kevin Street', 'Store Streeet',
              'Kilmainham', 'Mountjoy', 'Fitzgibbon Street',
              'Pearse Street', 'Irishtown', 'Donnybrook']

data = {}
for i in range(9):
    temp = {}
    temp['gid'] = i + 1
    temp['name'] = garda_list[i]
    temp['n_police'] = random.randint(1, 5) * 10
    temp['n_car'] = random.randint(3, 10)
    temp['n_ava_police'] = max(temp['n_police'] - random.randint(1, 5) * 5, 0)
    temp['n_ava_car'] = max(temp['n_car'] - random.randint(3, 10), 0)
    temp['latitude'] = 53.35 + random.random() % 0.03
    temp['longitude'] = -6.26 + random.random() % 0.03
    data['Garda' + str(i + 1)] = temp
final_data['Garda'] = data

# Hospital
hospital_list = ['St.Luke\'s Hospital', 'The Royal Hospital Donnybrook',
                 'Rotunda Hospital', 'Sir Patrick Dun\'s Hospital',
                 'The Mater misericordiae University Hospital',
                 'Bon Secours Hospital Dublin']

data = {}
for i in range(6):
    temp = {}
    temp['hid'] = i + 1
    temp['name'] = hospital_list[i]
    temp['n_doctor'] = random.randint(1, 5) * 10
    temp['n_ambulance'] = random.randint(3, 10)
    temp['n_ava_doctor'] = max(temp['n_doctor'] - random.randint(1, 5) * 5, 0)
    temp['n_ava_ambulance'] = max(temp['n_ambulance'] - random.randint(3, 10), 0)
    temp['latitude'] = 53.35 + random.random() % 0.03
    temp['longitude'] = -6.26 + random.random() % 0.03
    data['Hospital' + str(i + 1)] = temp
final_data['Hospital'] = data


# IndentificationCode
def code_generate():
    letter_set = [chr(i) for i in range(48, 58)]
    letter_set.extend([chr(i) for i in range(65, 91)])
    letter_set.extend([chr(i) for i in range(97, 123)])
    return ''.join(random.choices(letter_set, k=16))


data = {}
for i in range(200):
    temp['cid'] = i + 1
    temp['number'] = code_generate()
    temp['group'] = random.randint(1, 6)
    data['IdentificationCode' + str(i + 1)] = temp
final_data['IndentificationCode'] = data


# ReportInfo
def time_generate():
    h_time = time.time() - random.randint(0, 86400) * 60
    r_time = h_time + random.randint(600, 7200)
    # res = (time.strftime("%d-%m-%y %H-%M-%S", time.localtime(h_time)),
    #         time.strftime("%d-%m-%y %H-%M-%S", time.localtime(r_time)))
    res = (int(h_time) * 1000, int(r_time) * 1000)
    return res


def name_generate():
    letter_set = [chr(i) for i in range(65, 91)]
    letter_set.extend([chr(i) for i in range(97, 123)])
    length = random.randint(4, 16)
    return ''.join(random.choices(letter_set, k=length))

# Report
data = {}
for i in range(200):
    times = time_generate()
    temp = {}
    temp['rid'] = i + 1
    temp['report_type'] = disaster_type[random.randint(0,2)]
    temp['location'] = name_generate()
    temp['htime'] = times[0]
    temp['rtime'] = times[1]
    temp['latitude'] = 53.35 + random.random() % 0.03
    temp['longitude'] = -6.26 + random.random() % 0.03
    temp['report_state'] = random.randint(0, 1)
    temp['description'] = description_generate()
    temp['injury'] = injure[random.randint(0, 6)]
    data['Report' + str(i + 1)] = temp
final_data['Report'] = data

# Task
dp = ['fire','water','other']
data = {}
for i in range(1000):
    temp = {}
    temp['uid'] = i + 1
    temp['location'] = location_generate()
    temp['disasterType'] = dp[random.randint(0,2)]
    temp['otime'] = otime_generate()
    temp['longitude'] = 53.35 + random.random() - 0.5
    temp['latitude'] = -6.26 + random.random() - 0.5
    temp['injury'] = random.randint(0, 5)
    temp['task'] = str("Please go to the ** place")
    data['TaskInfo' + str(i + 1)] = temp

final_data['TaskInfo'] = data

# Train
station_list = ['Abbey Street', 'Jervis', 'Marlborough', 'Trinity',
                'George\'s Dock', 'Dawson', 'O\'Connell Upper',
                'St. Stephen\'s Green']
data = {}
for i in range(50):
    temp = {}
    temp['tid'] = i + 1
    temp['name'] = station_list[random.randint(0, 6)]
    temp['train_type'] = random.randint(1, 8)
    temp['train_available'] = random.randint(0, 1)
    temp['latitude'] = 53.35 + random.random() % 0.03
    temp['longitude'] = -6.26 + random.random() % 0.03
    data['Train' + str(i + 1)] = temp
final_data['Train'] = data


# UserInfo
def phone_generate():
    num_set = [chr(i) for i in range(48, 58)]
    return '0' + ''.join(random.choices(num_set, k=9))


def mail_generate():
    letter_set = [chr(i) for i in range(48, 58)]
    letter_set.extend([chr(i) for i in range(65, 91)])
    letter_set.extend([chr(i) for i in range(97, 123)])
    length = random.randint(5, 12)
    return ''.join(random.choices(letter_set, k=length)) + "@gmail.com"


def name_generate():
    letter_set = [chr(i) for i in range(65, 91)]
    letter_set.extend([chr(i) for i in range(97, 123)])
    length = random.randint(4, 16)
    return ''.join(random.choices(letter_set, k=length))


def r_time_generate():
    random_time = time.time() - random.randint(0, 86400) * 60
    return time.strftime("%d-%m-%y %H-%M-%S", time.localtime(random_time))


def password_gen():
    char_set = [chr(i) for i in range(33, 91)]
    char_set.extend([chr(i) for i in range(97, 123)])
    length = random.randint(8, 16)
    return ''.join(random.choices(char_set, k=length))


data = {}
for i in range(1000):
    temp = {}
    temp['uid'] = i + 1
    temp['name'] = name_generate()
    temp['r-time'] = r_time_generate()
    temp['password'] = password_gen()
    temp['mail'] = mail_generate()
    temp['phone'] = phone_generate()
    temp['type'] = random.randint(1, 4)
    data['UserInfo' + str(i + 1)] = temp
final_data['UserInfo'] = data

# BusStopInfo
# read csv
df = pd.read_csv('stops.csv')

# consider every line as a node
nodes = {}
for i, row in enumerate(df.itertuples(index=False)):
    node = {'BusID': i + 1}
    node.update(row._asdict())
    nodes[f'BusID_{i + 1}'] = node

# update final_data with BusStopInfo
final_data.update({'BusStopInfo': nodes})

# test available offcier
data = {}
for i in range(50):
    temp = {}
    temp['uid'] = 2000+i
    temp['type'] = random.randint(0,3)
    data["aaaa"+str(i)] = temp
final_data["AvailableOfficer"] = data

temp = {}
temp["Availble_Token"] = 2
temp["refillRate"] = 3600000
temp["lastRefillTime"] = int(time.time()) * 1000
final_data["TokenBuket"] = temp

# Output
with open('Data.json', 'w') as f:
    json.dump(final_data, f)
    f.close()
