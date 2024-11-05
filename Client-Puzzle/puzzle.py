from collections import defaultdict
from itertools import product

class Puzzle:
    def __init__(self, num_sub_puzzles, k):
        self.num_sub_puzzles = num_sub_puzzles  # Number of sub-puzzles
        self.k = k  # Maximum expected hash value for each sub-puzzle
        self.distributions = [defaultdict(int) for _ in range(num_sub_puzzles)]
        self.total_combinations = 0

    def calculate_distributions(self):
        # Generate all possible combinations for sub-puzzle values (from 0 to k for each sub-puzzle)
        all_combinations = product(range(self.k + 1), repeat=self.num_sub_puzzles)
        
        # Iterate through each combination
        for combination in all_combinations:
            # For each sub-puzzle, count the frequency of the values in this combination
            for sub_puzzle_index, value in enumerate(combination):
                self.distributions[sub_puzzle_index][value] += 1
            
            # Increase the count of total combinations
            self.total_combinations += 1

    def display_results(self):
        print(f"\nPuzzle with {self.num_sub_puzzles} sub-puzzles, k = {self.k}")
        print("Distributions per sub-puzzle after all combinations:")
        for i, dist in enumerate(self.distributions):
            print(f"Sub-puzzle {i + 1}:")
            for value, frequency in sorted(dist.items()):
                print(f"  Value {value}: {frequency} occurrences")
        print(f"Total combinations (worst-case): {self.total_combinations}")

# Create instances for:
# Puzzle A: 1 sub-puzzles, k = 6;
# Puzzle B: 6 sub-puzzles, k = 4.
puzzle_a = Puzzle(num_sub_puzzles=1, k=6)
puzzle_b = Puzzle(num_sub_puzzles=6, k=4)

# Calculate distributions and combinations for each puzzle
puzzle_a.calculate_distributions()
puzzle_b.calculate_distributions()

# Display results
puzzle_a.display_results()
puzzle_b.display_results()
