import pandas as pd
from itertools import product, combinations

class Puzzle:
    def __init__(self, id, num_sub_puzzles, k):
        self.id = id
        self.num_sub_puzzles = num_sub_puzzles
        self.k = k
        self.worst_expected_hashes = self.num_sub_puzzles * (self.k ** 2)
        self.distributions = self.distribute_possible_combinations()

    def distribute_possible_combinations(self):
        distribution_list = {
            "worst_expected_hash": self.worst_expected_hashes,
            "distributions": [], # Initialize empty array to store dictionaries
            "total_combination": 0 #iniatialize total combination
        }

        ALL_COMBINATIONS = set()  # Initialize empty set to store possible combinations in current hash
        for i in range(self.num_sub_puzzles, self.worst_expected_hashes + 1):
            if i <= (self.k):
                possible_combinations = product(range(1, (i + 1)), repeat=self.num_sub_puzzles)
            else:
                possible_combinations = product(range(1, (self.k + 1)), repeat=self.num_sub_puzzles)
            
            # Initialize dictionary to store the possible combinations for current distribution
            current_distribution = {
                "expected_hash": i,
                "combinations": [],
                "num_of_combinations": 0,
                "frequency_summary": {}  # To store the combinatorial counts for each case
            }

            for combo in possible_combinations:
                if combo not in ALL_COMBINATIONS:
                    current_distribution["combinations"].append(combo)
                    ALL_COMBINATIONS.add(combo)

            # Count frequencies based on combinations within the hash level
            frequencies = self.calculate_frequencies(current_distribution["combinations"])
            current_distribution["frequency_summary"] = frequencies
            current_distribution["num_of_combinations"] = len(current_distribution["combinations"])

            # Update current distribution to distribution list
            distribution_list["distributions"].append(current_distribution)
            distribution_list["total_combination"] = len(ALL_COMBINATIONS)
        
        return distribution_list

    def calculate_frequencies(self, combinations_list):
        # This function calculates frequencies of each "case" for combinations.
        # Here you need to apply logic like the image provided.
        # For each combination, we will count the occurrence of patterns (e.g., {3, 1, 1, 1})

        frequency_dict = {}
        for combo in combinations_list:
            pattern = tuple(sorted(combo, reverse=True))
            if pattern in frequency_dict:
                frequency_dict[pattern] += 1
            else:
                frequency_dict[pattern] = 1
        return frequency_dict

    def display_results(self):
        # Initialize a list to store table data for frequency summary
        table_data = []
        
        # Print the worst expected hash and total combinations
        print(f"Worst Expected Hash: {self.distributions['worst_expected_hash']}")
        print(f"Total possible combinations [Total Frequency (F)]: {self.distributions['total_combination']}")
        print("\n" + "-" * 30)
        
        # Display each distribution's frequency summary in tabular format
        for distribution in self.distributions["distributions"]:
            print(f"Expected Hash: {distribution['expected_hash']}")
            print(f"Number of Combinations [Frequency (f)]: {distribution['num_of_combinations']}")
            
            # Collect data for the table
            for pattern, freq in distribution["frequency_summary"].items():
                row = {
                    "Expected Hash": distribution["expected_hash"],
                    "Pattern": pattern,
                    "Frequency": freq
                }
                table_data.append(row)

            # Convert combination tuples to single strings
            combination_strings = ["".join(map(str, combo)) for combo in distribution["combinations"]]
            print("Current Combinations:")
            print(f"[{', '.join(combination_strings)}]")  # Display as a single line within brackets
            print("-" * 30)

        # Convert the table data to a DataFrame and display as a table
        df = pd.DataFrame(table_data)
        print("\nFrequency Summary Table:")
        print(df.to_string(index=False))

# Create an instance of the puzzle to test
puzzle_b = Puzzle('B', num_sub_puzzles=6, k=4)
puzzle_b.display_results()
