import matplotlib.pylab as plt

def fps_time(timestamp, fps):
    plt.plot(timestamp, fps, 'r-o')
    mark = timestamp[-1]
    plt.xlabel("Tiempo (ms) ")
    plt.ylabel("Fraccion de particulas en recinto izquierdo")
    plt.plot(mark, fps[-1], 'k-o', markevery=mark)
    plt.text(mark, fps[-1], mark, size=10)
    plt.show()


def fps_time_comparative(timestamp1, fps1, timestamp2, fps2, timestamp3, fps3):
    mark1 = timestamp1[-1]
    mark2 = timestamp2[-1]
    mark3 = timestamp3[-1]
    plt.plot(timestamp1, fps1, 'b-o', label="Simulacion 1")
    plt.plot(timestamp2, fps2, 'g-o', label="Simulacion 2")
    plt.plot(timestamp3, fps3, 'r-o', label="Simulacion 3")
    plt.legend()
    plt.plot(mark1, fps1[-1], "k-o", markevery=mark1)
    plt.plot(mark2, fps2[-1], "k-o", markevery=mark2)
    plt.plot(mark3, fps3[-1], "k-o", markevery=mark3)
    plt.text(mark1, fps1[-1], mark1, size=10)
    plt.text(mark2, fps2[-1], mark2, size=10)
    plt.text(mark3, fps3[-1], mark3, size=10)
    plt.xlabel("Tiempo (ms) ", fontsize=12)
    plt.ylabel("Fraccion de particulas en recinto izquierdo", fontsize=12)
    plt.show()
