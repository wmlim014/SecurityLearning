from itertools import product

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
                possible_combinations = product(range(1, (i + 1)), repeat = self.num_sub_puzzles)
            else:
                possible_combinations = product(range(1, (self.k + 1)), repeat = self.num_sub_puzzles)
            
            # Initialize dictionary to store the possible combinations for current distribution
            current_distribution = {
                "expected_hash": i,
                "combinations": [],
                "num_of_combinations": 0
            }

            for combo in possible_combinations:
                # Ensure unique combinations are added only once
                if combo not in ALL_COMBINATIONS:
                    current_distribution["combinations"].append(combo)
                    ALL_COMBINATIONS.add(combo)
            current_distribution["num_of_combinations"] = len(current_distribution["combinations"])
            
            # Update current distribution to distribution list
            distribution_list["distributions"].append(current_distribution)      
            distribution_list["total_combination"] = len(ALL_COMBINATIONS)
        
        return distribution_list
    
    #########################
    #   OUTPUT FUNCTIONS    #
    #########################
    split_line = "-" * 60    
    def display_results(self):
        # Print the output in a readable format
        print(f"Puzzle {self.id}")
        print(f"{self.split_line}")
        
        print("\nDistributions:")
        for distribution in self.distributions["distributions"]:
            print(f"Expected Hash: {distribution['expected_hash']}")
            print(f"Number of Combinations [Frequency (f)]: {distribution['num_of_combinations']}")
            
            # Display combinations in if any
            if distribution['num_of_combinations'] > 0:
                # Sort the combinations
                sorted_combinations = sorted(distribution["combinations"])
                print("Current Combinations:")
                print(f"{sorted_combinations}")

            print(f"{self.split_line}\n")
            
        print(f"Worst Expected Hash: {self.distributions['worst_expected_hash']}")
        print(f"Total Combinations: {self.distributions['total_combination']}")

    def write_output_file(self):
        file_name = f"Puzzle{self.id}.txt"

        # Open the file in write mode
        with open(file_name, 'w') as file:
            # Write the output in a readable format to the file
            file.write(f"Puzzle {self.id}\n")
            file.write(f"{self.split_line}\n")
            
            file.write("\nDistributions:\n")
            for distribution in self.distributions["distributions"]:
                file.write(f"Expected Hash: {distribution['expected_hash']}\n")
                file.write(f"Number of Combinations [Frequency (f)]: {distribution['num_of_combinations']}\n")
                
                # Display combinations if any
                if distribution['num_of_combinations'] > 0:
                    # Sort the combinations
                    sorted_combinations = sorted(distribution["combinations"])
                    file.write("Current Combinations:\n")
                    file.write(f"{sorted_combinations}\n")
                
                file.write(f"{self.split_line}\n")
                
            file.write(f"Worst Expected Hash: {self.distributions['worst_expected_hash']}\n")
            file.write(f"Total Combinations: {self.distributions['total_combination']}\n")

        print(f"Output written to {file_name}")

# Create instances for:
# Puzzle A: 1 sub-puzzles, k = 6;
# Puzzle B: 6 sub-puzzles, k = 4.
puzzle_a = Puzzle('A', num_sub_puzzles=1, k=6)
puzzle_b = Puzzle('B', num_sub_puzzles=6, k=4)

puzzle_a.display_results()
puzzle_b.display_results()

puzzle_a.write_output_file()
puzzle_b.write_output_file()