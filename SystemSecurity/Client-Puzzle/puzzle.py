import pandas as pd
from itertools import product, combinations

class Puzzle:
    def __init__(self, id, num_sub_puzzles, k):
        self.id = id
        self.num_sub_puzzles = num_sub_puzzles
        self.k = k
        self.worst_expected_hashes = self.num_sub_puzzles * self.compute_max_attempts_per_sub_puzzle()

    def compute_max_attempts_per_sub_puzzle(self):
        return 2 ** self.k
    
    def single_sub_puzzle(self, i):
        if i <= self.k:
            combination = 1
        else:
            combination = 0
        print(f"{i}, {combination}") #Debug
    
    def multiple_sub_puzzles(self, i):
        max_attempts_per_sub_puzzle = self.compute_max_attempts_per_sub_puzzle()
        distributions = [
            combination for combination in product(range(1, (max_attempts_per_sub_puzzle + 1)), 
                                                   repeat=self.num_sub_puzzles)
            if sum(combination) == i
        ]
        print(f"{i}, {len(distributions)}") #Debug

    def distribute_possible_combinations(self):

        for i in range(1, self.worst_expected_hashes + 1):
            if self.num_sub_puzzles == 1:
                self.single_sub_puzzle(i)
            else:
                self.multiple_sub_puzzles(i)

        print("-" * 30)

# Create instances for:
# Puzzle A: 1 sub-puzzles, k = 6;
puzzle_a = Puzzle('A', num_sub_puzzles=1, k=6)
puzzle_a.distribute_possible_combinations()

# Puzzle B: 6 sub-puzzles, k = 4.
puzzle_b = Puzzle('B', num_sub_puzzles=6, k=4)
puzzle_b.distribute_possible_combinations()