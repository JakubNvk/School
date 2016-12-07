import csv
import numpy as np
import matplotlib.pyplot as plt


def lookahead_moving_average(data, window):
    ma = []
    for i in range(len(data)):
        avg = 0
        if i + window > len(data):
            window -= 1
        for j in range(window):
            avg += data[i+j]
        ma.append(avg/float(window))
    return np.array(ma, dtype=float)


def single_exp_smoothing(data, alpha):
    S_t = data[0]
    ses = []
    for d in data:
        S_t = alpha * d + (1 - alpha) * S_t
        ses.append(S_t)
    return np.array(ses, dtype=float)


def double_exp_smoothing(data, alpha, gamma):
    S_t = data[0]
    b_t = data[1] - data[0]
    S_tt, b_tt = 0, 0
    des = []
    for d in data:
        S_tt = alpha * d + (1 - alpha) * S_t
        b_tt = gamma * (S_tt - S_t) + (1 - gamma) * b_t
        S_t = S_tt
        b_t = b_tt
        des.append(S_tt + b_tt)
    return np.array(des, dtype=float)


d = []
with open('data.csv', 'r') as csvfile:
    reader = csv.reader(csvfile, delimiter=',')
    for row in reader:
        d.append(row)
dataset = np.array(d, dtype=float)

time = dataset[:, 0]
data = dataset[:, 1]

plt.figure(1)
plt.subplot(511)
plt.title('look head moving average')
plt.plot(time, data, color='black', label='raw', lw=0.3)
ma = lookahead_moving_average(data, window=151)
plt.plot(time, data-ma, color='red', label='window=151', lw=1)
plt.ylabel('data')
plt.legend(bbox_to_anchor=(1.005, 1), loc=2, borderaxespad=0.)

plt.subplot(512)
plt.title('single exponential smoothing')
colours = ['red', 'blue', 'green', 'yellow']
alphas = [0.2, 0.3, 0.5, 0.8]
plt.plot(time, data, color='black', label='raw', lw=0.3)
for i in range(len(alphas)):
    ses = single_exp_smoothing(data, alpha=alphas[i])
    a_label = 'alpha={}'.format(alphas[i])
    plt.plot(time, data-ses, color=colours[i], label=a_label)
plt.legend(bbox_to_anchor=(1.005, 1), loc=2, borderaxespad=0.)

plt.subplot(513)
plt.title('double exponential smoothing for alpha = .2')
plt.plot(time, data, color='black', label='raw', lw=0.3)

des = double_exp_smoothing(data, 0.2, 0.2)
plt.plot(time, data-des, color='blue', label='.2, .2')
des = double_exp_smoothing(data, 0.2, 0.5)
plt.plot(time, data-des, color='green', label='.2, .5')
des = double_exp_smoothing(data, 0.2, 0.8)
plt.plot(time, data-des, color='red', label='.2, .8')
plt.legend(bbox_to_anchor=(1.005, 1), loc=2, borderaxespad=0.)

plt.subplot(514)
plt.title('double exponential smoothing for alpha = .5')
plt.plot(time, data, color='black', label='raw', lw=0.3)
des = double_exp_smoothing(data, 0.5, 0.2)
plt.plot(time, data-des, color='blue', label='.5, .2')
des = double_exp_smoothing(data, 0.5, 0.5)
plt.plot(time, data-des, color='green', label='.5, .5')
des = double_exp_smoothing(data, 0.5, 0.8)
plt.plot(time, data-des, color='red', label='.5, .8')
plt.legend(bbox_to_anchor=(1.005, 1), loc=2, borderaxespad=0.)

plt.subplot(515)
plt.title('double exponential smoothing for alpha = .8')
plt.plot(time, data, color='black', label='raw', lw=0.3)
des = double_exp_smoothing(data, 0.8, 0.2)
plt.plot(time, data-des, color='blue', label='.8, .2')
des = double_exp_smoothing(data, 0.8, 0.5)
plt.plot(time, data-des, color='green', label='.8, .5')
des = double_exp_smoothing(data, 0.8, 0.8)
plt.plot(time, data-des, color='red', label='.8, .5')
plt.legend(bbox_to_anchor=(1.005, 1), loc=2, borderaxespad=0.)

plt.show()
