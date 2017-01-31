import random
import math
from collections import defaultdict
from matplotlib import pyplot


MAX_ITERATIONS = 1000

def load_dataset(filename):
    with open(filename, 'r') as file:
        dataset = []

        for line in file:
            coordinates = line.split()
            vector = (float(coordinates[0]), float(coordinates[1]))
            dataset.append(vector)

        return dataset


def kMeans(k, dataset):
    centroids = init_random_centroids(k, dataset)

    iterations = 0
    old_centroids = None

    while not has_converged(centroids, old_centroids, iterations):
        iterations += 1
        old_centroids = centroids

        clusters = assign_to_clusters(dataset, centroids)

        centroids = recalculate_centroids(clusters)

    show_clusters(clusters, centroids)
    # return clusters


def show_clusters(clusters, centroids):
    colormap = ['blue', 'green', 'red', 'cyan', 'magenta', 'yellow', 'black',
                'lime', 'brown', 'orange', 'sage', 'aquamarine', 'grey',
                'skyblue', 'navy', 'plum', 'purple', 'pink']

    for cluster in clusters:
        points = clusters[cluster]
        x = [point[0] for point in points]
        y = [point[1] for point in points]

        color = None
        if cluster >= len(colormap):
            color = 'black'
        else:
            color = colormap[cluster]

        pyplot.scatter(x, y, c=color)

    for i, centroid in enumerate(centroids):
        color = None
        if i >= len(colormap):
            color = 'black'
        else:
            color = colormap[i]
        pyplot.scatter(centroid[0], centroid[1], c=color, marker='x', linewidths=2)

    pyplot.title('k-Means Clustering')
    pyplot.show()


def recalculate_centroids(clusters):
    centroids = []

    for cluster in clusters:
        points = clusters[cluster]

        sum_x = sum(point[0] for point in points)
        sum_y = sum(point[1] for point in points)

        mean_x = sum_x / len(points)
        mean_y = sum_y / len(points)

        centroid = mean_x, mean_y
        centroids.append(centroid)

    return centroids


def assign_to_clusters(dataset, centroids):
    clusters = defaultdict(list)

    for data_record in dataset:
        distances = []
        for i, centroid in enumerate(centroids):
            distance = euclidean_distance(data_record, centroid)
            distances.append((i, distance))
        best_mu = min(distances, key=lambda x: x[1])

        clusters[best_mu[0]].append(data_record)

    return clusters


def euclidean_distance(first_point, second_point):
    distance = 0
    for i in range(len(first_point)):
        distance += (float(second_point[i]) - float(first_point[i]))**2
    return math.sqrt(distance)


def has_converged(centroids, old_centroids, iterations):
    if iterations > MAX_ITERATIONS:
        return True
    return old_centroids == centroids


def init_random_centroids(k, dataset):
    centroids = random.sample(dataset, k)
    return centroids


normal_dataset = load_dataset('normal.txt')
unbalance_dataset = load_dataset('unbalance.txt')

# kMeans(5, normal_dataset)
kMeans(4, unbalance_dataset)
