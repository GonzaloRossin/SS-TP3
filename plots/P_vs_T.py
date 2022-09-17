import matplotlib.pylab as plt


def gases_ideales(pressures, pressures_error, energies):
    plt.scatter(energies, pressures)
    plt.errorbar(energies, pressures, yerr=pressures_error, fmt='o')
    plt.ylabel('Presión (N/m)', fontsize=12)
    plt.xlabel('Energía Cinetica (J)', fontsize=12)
    plt.show()
