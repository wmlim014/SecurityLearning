from itertools import product

'''
min = 1
max = 4
time_repeat = 3
no_of_combinations = 0

# Simple Test
for j in product(range(min, max + 1), repeat = time_repeat):
  no_of_combinations += 1
  print(j)

print("Total No.of combinations: ", +no_of_combinations)
'''

'''
When min = 1, max = 3, time_repeat = 2
with 
for j in product(range(min, max), repeat = time_repeat):
    Sample output:
    (1, 1)
    (1, 2)
    (2, 1)
    (2, 2)
    Total No.of combinations:  4

When min = 1, max = 3, time_repeat = 2
with
for j in product(range(min, max + 1), repeat = time_repeat):
    Sample output:
    (1, 1)
    (1, 2)
    (1, 3)
    (2, 1)
    (2, 2)
    (2, 3)
    (3, 1)
    (3, 2)
    (3, 3)
    Total No.of combinations:  9

When min = 1, max = 2, time_repeat = 3
with
for j in product(range(min, max + 1), repeat = time_repeat):

    Sample output:
    (1, 1, 1)
    (1, 1, 2)
    (1, 2, 1)
    (1, 2, 2)
    (2, 1, 1)
    (2, 1, 2)
    (2, 2, 1)
    (2, 2, 2)
    Total No.of combinations:  8
'''

# Test with each expected hash (E)
num_sub_puzzles = 4
k = 6
worst_hashes = num_sub_puzzles * (k ** 2) # Worst Expected Hashes (WE) = number of sub-puzzle * (2^k)
total_combinations = 0

print(f"Worst Expected Hashes: {worst_hashes}")

# Loop for each hashes
for i in range(num_sub_puzzles, worst_hashes + 1):
    no_of_combinations = 0    # Reset number of possible combination for current hash

    for j in product(range(1, k + 1), repeat = num_sub_puzzles):
        no_of_combinations += 1
        print(j)

    print(f"No.of possible combinations [Frequency (F)] for {i} hashes: {no_of_combinations}")
    total_combinations += no_of_combinations

print(f"Total possible combinations [Total Frequency (F)]: {total_combinations}")