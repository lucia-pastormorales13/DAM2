import threading
import time
counter = 0 # Counter is a global shared variable, we initialize it to 0
# We use a Lock to prevent race conditions
lock = threading.Lock()
def increase_counter():
    # As we are modifying a global variable, we need to declare it as global
    # inside the function or python will treat it as a local variable
    global counter
    for _ in range(5):
        # Ask for the lock BEFORE entering the critical section
        lock.acquire()
    try:
        # CRITICAL SECTION: Only one thread here at a time
        print(f"Thread {threading.current_thread().name} incrementing counter.")
        counter = counter + 1
    finally:
        # Ensure the lock is released even if an error occurs
        lock.release()
        time.sleep(1) #Simulates some processing time for parallelism
    if __name__ == "__main__":
        # Create and start two threads. Later wait for them to finish
        t1 = threading.Thread(target=increase_counter)
        t2 = threading.Thread(target=increase_counter)
        # Start the two threads
        t1.start()
        t2.start()
        # Wait for them to finish
        t1.join()
        t2.join()
        # The final value of the counter should be 10
print(f"Counter final value (using Lock): {counter}")