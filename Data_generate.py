import json
import random
import time

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
    temp['longitude'] = 53.35 + random.random() - 0.5
    temp['latitude'] = -6.26 + random.random() - 0.5
    data['Bus' + str(i + 1)] = temp
final_data['Bus'] = data

# DisasterInfo
disaster_type = ["fire", "water", "general"]
radius = [5, 10, 20, 50, 100, 200]

def location_generate():
    letter_set = [chr(i) for i in range(65, 91)]
    letter_set.extend([chr(i) for i in range(97, 123)])
    length = random.randint(4, 16)
    return ''.join(random.choices(letter_set, k=length))


def otime_generate():
    random_time = time.time() - random.randint(0, 86400) * 60
    return int(random_time) * 1000


data = {}
for i in range(1000):
    temp = {}
    temp['did'] = i + 1
    temp['location'] = location_generate()
    temp['radius'] = radius[random.randint(0,5)]
    temp['disasterType'] = random.randint(1,3)
    temp['otime'] = otime_generate()
    temp['longitude'] = 53.35 + random.random() - 0.5
    temp['latitude'] = -6.26 + random.random() - 0.5
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
    temp['longitude'] = 53.35 + random.random() - 0.5
    temp['latitude'] = -6.26 + random.random() - 0.5
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
    temp['longitude'] = 53.35 + random.random() - 0.5
    temp['latitude'] = -6.26 + random.random() - 0.5
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
    temp['longitude'] = 53.35 + random.random() - 0.5
    temp['latitude'] = -6.26 + random.random() - 0.5
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


data = {}
for i in range(200):
    times = time_generate()
    temp = {}
    temp['rid'] = i + 1
    temp['report_type'] = random.randint(1, 3)
    temp['location'] = name_generate()
    temp['htime'] = times[0]
    temp['rtime'] = times[1]
    temp['longitude'] = 37.7749 + random.random() % 0.05
    temp['latitude'] = -122.4194 + random.random() % 0.05
    temp['report_state'] = random.randint(-1, 1)
    data['Report' + str(i + 1)] = temp
final_data['Report'] = data

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
    temp['longitude'] = 53.35 + random.random() - 0.5
    temp['latitude'] = -6.26 + random.random() - 0.5
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

# Output
with open('Data.json', 'w') as f:
    json.dump(final_data, f)
    f.close()