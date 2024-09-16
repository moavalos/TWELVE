INSERT INTO Usuario(id, email, password, rol, activo)
VALUES (null, 'test@unlam.edu.ar', 'test', 'ADMIN', true);

INSERT INTO Movie (nombre, descripcion, frase, duracion, pais, cantVistas, idCategoria, añoLanzamiento, imagen, likes,
                   valoracion)
VALUES ('El Origen',
        'Un thriller que desafía la mente sobre la infiltración en los sueños.',
        'Tienes que tener miedo de soñar un poco más grande, querido.',
        148.0,
        'EE.UU.',
        5000000,
        1, -- SUSPENSO
        '2010-07-16',
        'el_origen.jpg',
        35000,
        8.8);

INSERT INTO Movie (nombre, descripcion, frase, duracion, pais, cantVistas, idCategoria, añoLanzamiento, imagen, likes,
                   valoracion)
VALUES ('El Caballero de la Noche',
        'Cuando el peligroso Joker emerge, Batman debe enfrentar una de las mayores pruebas psicológicas y físicas.',
        '¿Por qué tan serio?',
        152.0,
        'EE.UU.',
        7000000,
        2, -- ACCION
        '2008-07-18',
        'el_caballero_de_la_noche.jpg',
        45000,
        9.0);

INSERT INTO Movie (nombre, descripcion, frase, duracion, pais, cantVistas, idCategoria, añoLanzamiento, imagen, likes,
                   valoracion)
VALUES ('Matrix',
        'Un hacker informático descubre la verdadera naturaleza de la realidad y su papel en la guerra contra sus controladores.',
        'No hay cuchara.',
        136.0,
        'EE.UU.',
        6500000,
        3, -- CIENCIA_FICCION
        '1999-03-31',
        'matrix.jpg',
        38000,
        8.7);

INSERT INTO Movie (nombre, descripcion, frase, duracion, pais, cantVistas, idCategoria, añoLanzamiento, imagen, likes,
                   valoracion)
VALUES ('Interestelar',
        'Un equipo de exploradores viaja a través de un agujero de gusano en el espacio para asegurar la supervivencia de la humanidad.',
        'La humanidad nació en la Tierra. Nunca estuvo destinada a morir aquí.',
        169.0,
        'EE.UU.',
        5500000,
        3, -- CIENCIA_FICCION
        '2014-11-07',
        'interestelar.jpg',
        42000,
        8.6);

INSERT INTO Movie (nombre, descripcion, frase, duracion, pais, cantVistas, idCategoria, añoLanzamiento, imagen, likes,
                   valoracion)
VALUES ('Parásitos',
        'La codicia y la discriminación de clases amenazan la relación simbiótica recién formada entre la rica familia Park y el empobrecido clan Kim.',
        'Actúa como si fueras dueño del lugar.',
        132.0,
        'Corea del Sur',
        4500000,
        4, -- DRAMA
        '2019-05-30',
        'parasitos.jpg',
        40000,
        8.6);

INSERT INTO Movie (nombre, descripcion, frase, duracion, pais, cantVistas, idCategoria, añoLanzamiento, imagen, likes,
                   valoracion)
VALUES ('Tiempos Violentos',
        'Las vidas de dos sicarios, un boxeador, un gánster y su esposa se entrelazan en cuatro historias de violencia y redención.',
        'Inglés, hijo de p***, ¿lo hablas?',
        154.0,
        'EE.UU.',
        6000000,
        5, -- CRIMEN
        '1994-10-14',
        'tiempos_violentos.jpg',
        50000,
        8.9);

INSERT INTO Categoria (id, nombre)
VALUES (1, 'SUSPENSO');
INSERT INTO Categoria (id, nombre)
VALUES (2, 'ACCION');
INSERT INTO Categoria (id, nombre)
VALUES (3, 'CIENCIA_FICCION');
INSERT INTO Categoria (id, nombre)
VALUES (4, 'DRAMA');
INSERT INTO Categoria (id, nombre)
VALUES (5, 'CRIMEN');