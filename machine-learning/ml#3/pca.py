import numpy as np
import matplotlib.pyplot as plt
from sklearn import decomposition, preprocessing

data = []
with open('some_20_dim_data_1000_rows.txt', 'r') as f:
    for line in f:
        d = [float(x) for x in line.split()]
        data.append(d)

# preprocessing
data = np.array(data)
data = preprocessing.scale(data)

# pca & pve
num_of_features = 20
pca = decomposition.PCA(n_components=num_of_features-1)
pca.fit(data)
pve = pca.explained_variance_ratio_
cum_pve = np.cumsum(pve)

plt.figure()
plt.subplot(121)
plt.title('PVE')
plt.plot(pve)
plt.subplot(122)
plt.title('Cumulative PVE')
plt.plot(cum_pve)
plt.show()
