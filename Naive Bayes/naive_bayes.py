import csv
import random
from collections import Counter


def load_dataset(filename):
    with open(filename, 'r') as csvfile:
        reader = csv.reader(csvfile)
        dataset = list(reader)
        return dataset


def ten_fold_cross_validation(dataset):
    for k in range(10):
        training_set = [x for i, x in enumerate(dataset) if i % 10 != k]
        test_set = [x for i, x in enumerate(dataset) if i % 10 == k]

        naive_bayes(training_set, test_set)
        print('-' * 80)


def naive_bayes(training_set, test_set):
    prior = calc_prior_probabilities(training_set)

    assigned_classes = []

    for test_instance in test_set:
        assigned_class = classify(training_set, test_instance, prior)
        assigned_classes.append(assigned_class)
        actual_class = test_instance[-1]
        print('Assigned class -> {}; Actual class -> {}'
               .format(assigned_class, actual_class))
    print('Accuracy: {0:.2f}%'.format(calc_accuracy(test_set, assigned_classes)))


def calc_prior_probabilities(training_set):
    prior = {}
    classes = [i[-1] for i in training_set]
    classes_counter = Counter(classes)
    for c in classes_counter:
        prior[c] = classes_counter[c] / float(len(classes))
    return prior


def conditional_probability(training_set, attr, attr_val, class_val):
    total = 0
    class_val_count = 0
    for training_instance in training_set:
        if training_instance[-1] != class_val:
            continue

        class_val_count += 1
        for i, val in enumerate(training_instance):
            if i != attr or i == len(training_instance) - 1:
                continue
            if val == attr_val:
                total += 1
    
    conditional_probability = total / float(class_val_count)
    return conditional_probability


def posterior_numerators(training_set, test_instance, prior):
    posterior = {}
    for c in prior:
        posterior_num = prior[c]
        for i, val in enumerate(test_instance):
            if i == len(test_instance) - 1:
                continue
            posterior_num *= conditional_probability(training_set, i, val, c)
        posterior[c] = posterior_num
    return posterior


def classify(training_set, test_instance, prior):
    posterior = posterior_numerators(training_set, test_instance, prior)
    best_probability = None
    best_class = None
    for c in posterior:
        if not best_probability or posterior[c] > best_probability:
            best_probability = posterior[c]
            best_class = c
    return best_class


def calc_accuracy(test_set, predicted_classes):
    accurate_predictions_count = 0
    for i, test_instance in enumerate(test_set):
        if test_instance[-1] == predicted_classes[i]:
            accurate_predictions_count += 1
    return (float(accurate_predictions_count) / len(test_set)) * 100


dataset = load_dataset('votes.data.csv')
ten_fold_cross_validation(dataset)
