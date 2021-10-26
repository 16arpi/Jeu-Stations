# Jeu des stations

![jeu des stations](./preview_play.png)

Jeu android pour retrouver toutes les stations de métro franciliennes.

## Règles du jeu

Vous devez trouver toutes les stations du métro parisien. Le jeu n'inclue pas les stations de tramway et de RER. La seule règle est la suivante : seules les stations présentes sur la ou les mêmes lignes que la station précédente peuvent être ajoutées.

Profitez ainsi des stations avec correspondances (point blanc à la bordure noire) pour changer de ligne. Vous pouvez à tout moment savoir quelles lignes passent par une station en cliquant dessus.

Même si le jeu accepte les erreurs de frappe, vous devez malgré tout écrire le nom complet des stations, comme ils figurent sur le plan de la RATP. Vous pouvez bénéficiez d'un indice en cliquant sur « ? ». Une liste de 4 stations dont une valide vous sera proposée. +1 en cas de victoire, -1 en cas de défaite.

## Points

* +1 pour une station avec correspondance
* +3 pour une station sans correspondance
* +10 pour une ligne complète
* –1 pour une station ajoutée plus d'une fois

## Utilisation de bibliothèques tierces

* [Google Components pour Android](https://github.com/material-components/material-components-android) - Apache License 2.0
* [Konfetti](https://github.com/DanielMartinus/Konfetti) - ISC License
* [GSON](https://github.com/google/gson) - Apache-2.0 License
* [Couleurs des lignes RATP](https://data.ratp.fr/explore/dataset/indices-et-couleurs-de-lignes-du-reseau-ferre-ratp/information/) - Licence RATP