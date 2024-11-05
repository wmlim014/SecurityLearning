######################################
#   Name           : Lim Wen Mi      #
#   Submission for : Assignment 1    #
#   File name      : Device.py       #
#   Date           : 28/10/2024      #
######################################

# import needed library
import sys
import time
import hashlib

# Function generate 6 OTP based on username, password and time interval
def generate_pin(username, password, interval):
    key = f"{username}{password}{interval}".encode()
    hash_value = hashlib.sha256(key).hexdigest()
    # As SHA256 hashing function generates 64-character hexadecimal value 
    # and it's a fixed-length representation of the username, password and time interval
    # So, need to convert the hash_value to 6-digit PIN
    pin = int(hash_value, 16) % 1000000
    return f"{pin:06}"

def main():
    # Check if the program was run with exactly three arguments
    if len(sys.argv) != 3:
        print("Usage: Device <username> <password>")
        sys.exit(1)

    # Initialize argument
    # sys.argv[0] is always the name of the script itself (e.g. Device.py)
    # Initialize sys.argv[1] and sys.argv[1] by the user respectively
    username = sys.argv[1]
    password = sys.argv[2]

    print("Starting Device PIN generator...")
    try:
        while True:
            # Compute the current time window (every 15 seconds)
            current_interval = int(time.time() // 15)
            pin = generate_pin(username, password, current_interval)
            print(f"Device: {pin}")
            time.sleep(15)
    except KeyboardInterrupt:
        print("\nDevice stopped.")

if __name__ == "__main__":
    main()