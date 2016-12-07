from sklearn import svm, datasets
from sklearn.model_selection import cross_val_score
from math import sqrt
import numpy as np


iris = datasets.load_iris()
X = iris.data[:, :2]
y = iris.target
C = 1.0

# kernels
rbf_ker = svm.SVC(kernel='rbf', gamma=0.7, C=C).fit(X, y)
poly_ker = svm.SVC(kernel='poly', degree=3, C=C).fit(X, y)

# kfold cv
score_rbf = cross_val_score(rbf_ker, iris.data, iris.target, cv=6,
                            scoring='f1_macro')
score_poly = cross_val_score(poly_ker, iris.data, iris.target, cv=6,
                             scoring='f1_macro')
# mitchell ttest
k = len(score_rbf)
delta_ = (1 / float(k)) * np.sum(score_rbf - score_poly)
err = 0
for i in range(k):
    err += ((score_rbf[i] - score_poly[i]) - delta_)**2
s_delta = sqrt(err / float(k*(k - 1)))

# confidence level of 90%, k-fold=6 (v=k-1); t_N,k-1 = 2.02
t = 2.02
print '[{}, {}]'.format(delta_ - t*s_delta, delta_ + t*s_delta)

# confidence level of 95%, k-fold=6 (v=k-1); t_N,k-1 = 2.57
t = 2.57
print '[{}, {}]'.format(delta_ - t*s_delta, delta_ + t*s_delta)

# confidence level of 98%, k-fold=6 (v=k-1); t_N,k-1 = 3.36
t = 3.36
print '[{}, {}]'.format(delta_ - t*s_delta, delta_ + t*s_delta)

# confidence level of 99%, k-fold=6 (v=k-1); t_N,k-1 = 4.03
t = 4.03
print '[{}, {}]'.format(delta_ - t*s_delta, delta_ + t*s_delta)
