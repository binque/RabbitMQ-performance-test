#RabbitMQ PERFORMANCE TEST IN POC3

---

###Code: 
https://github.com/binque/RabbitMQ-performance-test

---

###POC3 cpu info: 

```
model name	: Intel(R) Xeon(R) CPU E5-2650 v2 @ 2.60GHz
```

```
cat /proc/cpuinfo | grep "processor" | wc -l
8
```
```
cat /proc/cpuinfo | grep "physical id" | sort | uniq | wc -l
8
```
```
cat /proc/cpuinfo | grep "cpu cores" | uniq | awk -F: '{print $2}'
 1
 ```

---

###Result:

#####publish:

| queue number | connector number | cpu | process per second|
|---|---|---|---|
|1|1|200%|8158.34|
|1|4|200%|8158.34|
|1|6|440%|11283.625|
|1|8||9694.43|

| queue number | connector number | cpu | process per second|
|---|---|---|---|
|1|6||11767.47|
|2|6|400%|18536.025|
|4|6|650%|35931.156|
|6|6|700%|40615.734|
|8|6|700%|37411.15|

#####consumer:
| queue number | connector number | cpu | process per second|
|---|---|---|---|
|1|1|160%|28478.67|
|1|2|160%|27489.90|
|1|4|160%|38202|


| queue number | connector number | cpu | process per second|
|---|---|---|---|
|4|1|550%|79051.38|
|6|1|160%|83472.45|
|8|1|160%|37735.85|

---

