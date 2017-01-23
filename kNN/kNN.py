import csv
import random
import math
from collections import Counter


TEST_SET_COUNT = 20

def load_dataset(filename):
    with open(filename, 'r') as csvfile:
        reader = csv.reader(csvfile)
        dataset = set()

        for line in reader:
            line = tuple(line)
            dataset.add(line)
        return dataset


def get_training_and_test_sets(dataset):
    test_set = random.sample(dataset, TEST_SET_COUNT)
    training_set = [i for i in dataset if i not in test_set]
    return training_set, test_set


def euclidean_distance(first_instance, second_instance):
    distance = 0
    for i in range(len(first_instance) - 1):
        distance += (float(second_instance[i]) - float(first_instance[i]))**2
    return math.sqrt(distance)


def nearest_neighbors(k, test_instance, training_set):
    distances = []
    for training_instance in training_set:
        distance = euclidean_distance(test_instance, training_instance)
        distances.append((training_instance, distance))
    distances.sort(key=lambda x: x[1])

    neighbors = [distances[i][0] for i in range(k)]
    return neighbors


def get_predicted_class(neighbors):
    all_votes = [neighbor[-1] for neighbor in neighbors]
    votes_counter = Counter(all_votes)
    predicted_class = votes_counter.most_common(1)[0][0]
    return predicted_class


def calc_accuracy(test_set, predicted_classes):
    accurate_predictions_count = 0
    for i, test_instance in enumerate(test_set):
        if test_instance[-1] == predicted_classes[i]:
            accurate_predictions_count += 1
    return (float(accurate_predictions_count) / len(test_set)) * 100


def kNN(k):
    dataset = load_dataset('iris.data.csv')
    training_set, test_set = get_training_and_test_sets(dataset)

    predicted_classes = []

    for test_instance in test_set:
        neighbors = nearest_neighbors(k, test_instance, training_set)
        predicted_class = get_predicted_class(neighbors)
        predicted_classes.append(predicted_class)
        actual_class = test_instance[-1]
        print('Predicted class -> {}; Actual class -> {}'
              .format(predicted_class, actual_class))
    print('Accuracy: {0:.2f}%'.format(calc_accuracy(test_set, predicted_classes)))


k = 7
kNN(k)
