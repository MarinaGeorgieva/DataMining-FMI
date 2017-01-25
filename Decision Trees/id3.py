import csv
import random
import math
from collections import Counter


class ID3:
    def __init__(self, filename):
        self.dataset = self.load_dataset(filename)
        self.training_set = []
        self.test_set = []
        self.attributes = list(range(len(self.dataset[0]) - 1))

    def load_dataset(self, filename):
        with open(filename, 'r') as csvfile:
            reader = csv.reader(csvfile)
            dataset = list(reader)
            return dataset

    def ten_fold_cross_validation(self):
        for k in range(10):
            self.training_set = [x for i, x in enumerate(self.dataset)
                                 if i % 10 != k]
            self.test_set = [x for i, x in enumerate(self.dataset)
                             if i % 10 == k]

            self.apply_model()
            print('-' * 80)

    def apply_model(self):
        decision_tree = self.create_tree(self.training_set, self.attributes)

        assigned_classes = []

        for test_record in self.test_set:
            assigned_class = self.classify(test_record, decision_tree)
            assigned_classes.append(assigned_class)
            actual_class = test_record[-1]
            print('Assigned class -> {}; Actual class -> {}'
                  .format(assigned_class, actual_class))
        print('Accuracy: {0:.2f}%'
              .format(self.calc_accuracy(assigned_classes)))

    def classify(self, test_record, decision_tree):
        while True:
            attr = list(decision_tree.keys())[0]
            val = test_record[attr]
            decision_tree = decision_tree[attr][val]

            if isinstance(decision_tree, str):
                assigned_class = decision_tree
                return assigned_class

    def calc_accuracy(self, assigned_classes):
        accurate_predictions_count = 0
        for i, test_record in enumerate(self.test_set):
            if test_record[-1] == assigned_classes[i]:
                accurate_predictions_count += 1
        return (float(accurate_predictions_count) / len(self.test_set)) * 100

    # def print_tree(self):
    #     decision_tree = self.create_tree(self.training_set, self.attributes)
    #     print(decision_tree)

    def create_tree(self, examples, attributes, target_attr=-1):
        examples = examples[:]
        attributes = attributes[:]
        class_values = [example[target_attr] for example in examples]

        if len(attributes) <= 0:
            return self.default(class_values)
        elif class_values.count(class_values[0]) == len(class_values):
            return class_values[0]
        else:
            best = self.choose_attribute(examples, attributes)
            tree = {best: {}}

            for value in self.get_distinct_values(self.dataset, best):
                ex_subset = self.get_examples_subset(examples, best, value)

                if not ex_subset:
                    tree[best][value] = self.default(class_values)
                else:
                    attrs = [attr for attr in attributes if attr != best]

                    subtree = self.create_tree(ex_subset, attrs, target_attr)
                    tree[best][value] = subtree

        return tree

    def choose_attribute(self, examples, attributes, target_attr=-1):
        best_gain = None
        best_attr = None

        for example in examples:
            for attr in attributes:
                gain = self.gain(examples, attr, target_attr)

                if not best_gain or gain > best_gain:
                    best_gain = gain
                    best_attr = attr

        return best_attr

    def entropy(self, examples, target_attr=-1):
        class_values = [example[target_attr] for example in examples]
        classes_counter = Counter(class_values)
        classes_probability = {}
        entropy = 0

        for class_val in classes_counter:
            probability = classes_counter[class_val] / len(class_values)
            classes_probability[class_val] = probability

        for probability in classes_probability.values():
            entropy += -probability * math.log2(probability)

        return entropy

    def gain(self, examples, attr, target_attr=-1):
        attr_values = [example[attr] for example in examples]
        values_counter = Counter(attr_values)
        values_probability = {}
        subset_entropy = 0

        for val in values_counter:
            probability = values_counter[val] / len(attr_values)
            values_probability[val] = probability

        for val in values_probability:
            prob = values_probability[val]
            ex_subset = [ex for ex in examples if ex[attr] == val]
            subset_entropy += prob * self.entropy(ex_subset, target_attr)

        gain = self.entropy(examples, target_attr) - subset_entropy
        return gain

    def get_examples_subset(self, examples, attr, val):
        ex_subset = [example for example in examples if example[attr] == val]
        return ex_subset

    def get_distinct_values(self, examples, attr):
        values = {example[attr] for example in examples}
        return values

    def default(self, class_values):
        classes_counter = Counter(class_values)
        mode = classes_counter.most_common(1)[0][0]
        return mode


decision_tree = ID3('breast-cancer.csv')
decision_tree.ten_fold_cross_validation()
