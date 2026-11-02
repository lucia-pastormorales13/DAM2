import threading
import time

def my_message(message, times):
    """Función que imprime un mensaje un número determinado de veces."""
    for _ in range(times):
        print(f"{message} | Hilo actual: {threading.current_thread().name}")
        time.sleep(1)

if __name__ == '__main__':
    print("=== 1. EJECUCIÓN CON START() (Concurrente) ===")
    t1 = threading.Thread(target=my_message, args=("Hilo 1", 1))
    t2 = threading.Thread(target=my_message, args=("Hilo 2", 2))
    t3 = threading.Thread(target=my_message, args=("Hilo 3", 3))

    start_time_start = time.time()
    
    # Iniciar los hilos concurrentemente
    t1.start()
    t2.start()
    t3.start()

    print(f"Número de hilos activos con start(): {threading.active_count()}")

    # Esperar a que terminen
    t1.join()
    t2.join()
    t3.join()

    total_time_start = time.time() - start_time_start
    print(f"-> Tiempo total con start(): {total_time_start:.2f} segundos\n")


    print("=== 2. EJECUCIÓN CON RUN() (Secuencial) ===")
    # Creamos nuevos hilos (o se pueden reutilizar, pero es mejor instanciarlos de nuevo)
    r1 = threading.Thread(target=my_message, args=("Hilo 1 (run)", 1))
    r2 = threading.Thread(target=my_message, args=("Hilo 2 (run)", 2))
    r3 = threading.Thread(target=my_message, args=("Hilo 3 (run)", 3))

    start_time_run = time.time()

    # run() ejecuta la función síncronamente en el hilo principal
    r1.run()
    r2.run()
    r3.run()

    print(f"Número de hilos activos con run(): {threading.active_count()}")

    total_time_run = time.time() - start_time_run
    print(f"-> Tiempo total con run(): {total_time_run:.2f} segundos\n")


    print("=== RESUMEN Y COMPARACIÓN ===")
    print(f"Tiempo con start(): {total_time_start:.2f}s (Los hilos corren en paralelo).")
    print(f"Tiempo con run():   {total_time_run:.2f}s (Se ejecutan uno tras otro en el hilo principal).")