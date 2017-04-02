import numpy as np
import pandas as pd
from math import floor
import matplotlib.pyplot as plt


def logsig(X):
    return 1 / (1 + np.exp(np.negative(X)))


def dlogsig(X):
    return logsig(X) * (1 - logsig(X))


def forward(W_hid, W_out, x):
    print('W_hid: {}, W_out: {}, x: {}'.format(
        W_hid.shape, W_out.shape, x.shape))
    # x = np.concatenate((x, np.ones(x.shape[0])), axis=0) # x = [x; 1];
    print('x conca: {}'.format(x.shape))
    a = x.dot(W_hid)
    # h = [logsig(a); 1];
    h = np.concatenate((logsig(a), np.ones(x.shape[0])), axis=0)
    b = h.dot(W_out)
    y = logsig(b)  # mozno cisto b
    return y, b, h, a


def backward(W_hid, W_out, x, a, h, b, y, d):
    # x = np.concatenate((x, np.ones(x.shape[0])), axis=0) # x = [x; 1];
    g_out = (y - d) * dlogsig(b)
    # g_hid = ((W_out(:, 1:end-1))'*g_out) .* dlogsig(a);
    g_hid = ((W_out[:-1, :]).dot(g_out.T) * dlogsig(a).T)
    dW_hid = g_hid.dot(x)  # dW_hid = g_hid * x';
    dW_out = g_out.T.dot(h)  # dW_out = g_out * h';
    return dW_hid, dW_out


def load_data_from_file(file):
    X = np.loadtxt(file, usecols=(0, 1), skiprows=1)
    y = np.loadtxt(file, dtype='str', usecols=(2,), skiprows=1)
    y = pd.get_dummies(y).as_matrix()
    # print("Dataset shapes X: {}, y: {}".format(X.shape, y.shape))
    return X, y

# PARAMETERS
alpha = 0.05
dim_hid = 50
epochs = 10
k = 8

draw_CEs = 10
draw_REs = 10
draw_dots = 10

# LOAD DATA
X, y = load_data_from_file('2d.trn.dat')
full_set = np.concatenate((X, y), axis=1)
X_kfold = np.asarray(np.split(X, k))
y_kfold = np.asarray(np.split(y, k))

full_count, dim = full_set.shape

################################################
# SPLIT TRAINING DATA INTO ESTIMATION/VALIDATION
# train_X = X_kfold[]
# train_y = y_kfold[]
# val_X = X_kfold[]
# val_y = y_kfold[]
#
# normalisation
#mean = np.mean(train_X, axis=0)
#std = np.std(train_X, axis=0)
#train_X = (train_X - mean) / std
#val_X = (val_X - mean) / std


############################################
# np.random.shuffle(full_set)  # shuffle dataset
#split = floor(0.8 * full_count)
# split dataset
#est_set = full_set[:split, :]
#val_set = full_set[split:, :]
#est_count = est_set.shape[0]
#val_count = val_set.shape[0]

# X_train = full_set[:split, :2]
# y_train = full_set[:split, 2:]
# X_test = full_set[split:, :2]
# y_test = full_set[split:, 2:]

# INIT
#########################################################


#######################################################

# TRAIN
print('Training...')

for ith_fold in range(1):
    # pick train folds
    # np.reshape(divne_3d_pole, (7000, 2))
    train_range = [i for j in (range(0, ith_fold), range(ith_fold+1, k)) for i in j]
    X_train = X_kfold[train_range]
    y_train = y_kfold[train_range]
    val_X = X_kfold[ith_fold]
    val_y = y_kfold[ith_fold]

    # normalize
    X_train_min = X_train.min()
    X_train_max = X_train.max()

    X_train = (X_train - X_train_min) / X_train_max
    X_train = (X_train - .5) * 2
    val_X = (val_X - X_train_min) / X_train_min
    val_X = (val_X - .5) * 2

    # reshape
    X_train = np.reshape(X_train, (X_train.shape[0] * X_train.shape[1],
                                   X_train.shape[2]))
    X_train = np.column_stack((X_train, np.ones(len(X_train))))
    y_train = np.reshape(y_train, (y_train.shape[0] * y_train.shape[1],
                                   y_train.shape[2]))
    val_X = np.column_stack((val_X, np.ones(len(val_X))))

    # init
    dim_in = X_train.shape[1]
    dim_out = y.shape[1]
    W_hid = np.random.rand(dim_in, dim_hid) * 3 - 1.5
    W_out = np.random.rand(dim_hid + 1, dim_out) * 3 - 1.5
    estREs = []
    valREs = []
    total_error = []
    total_accuracy = []
    hid_zeros = np.zeros_like(W_hid)
    out_zeros = np.zeros_like(W_out)

    for i in range(epochs):
        print('>Epoch {}/{}: '.format(i, epochs))

        # shuffle
        shuffled_indexes = np.random.permutation(range(len(X_train)))
