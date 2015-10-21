import random
random.seed(0)

num_attrs = 100000
print "@relation massive"
for i in range(0, num_attrs):
    print "@attribute x%i numeric" % i
print "@attribute class {0,1}"
print "@data"

vector = [str(random.random()) for x in range(0, num_attrs)]
for j in range(0, int(num_attrs*0.5)):
    vector[j]="0"
random.shuffle(vector)
vector = vector + ["0"]
vector = ",".join(vector)


for i in range(0, 2000):
    print vector
