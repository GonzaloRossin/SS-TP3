import matplotlib.pylab as plt

def error_ajuste(pressures, energies, c_low, c_high):
    c_vals = []
    err_vals = []
    min_error = 10000
    min_c = 0
    for c in range(c_low, c_high):
        c_vals.append(c)
        err = 0
        for i in range(0, len(pressures)):
            err += (pressures[i] - c * energies[i]) ** 2

        if err < min_error:
            min_error = err
            min_c = c
        err_vals.append(err)

    plt.plot(c_vals, err_vals)
    plt.scatter(min_c, min_error, c='k', label='Minimum value for c')
    plt.legend()
    plt.text(min_c, min_error, min_c, size=10)
    plt.ylabel('Error', fontsize=12)
    plt.xlabel('Valor de Ajuste (c)', fontsize=12)
    plt.show()
    plt.scatter(energies, pressures)
    plt.axline((0, 0), slope=min_c)
    plt.ylabel('Presión (N/m)', fontsize=12)
    plt.xlabel('Energía Cinetica (J)', fontsize=12)
    plt.show()
