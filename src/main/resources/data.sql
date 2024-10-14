-- Inserts del Usuario
INSERT INTO Usuario (email, password, rol, activo, username)
VALUES ('test@unlam.edu.ar', 'test', 'ADMIN', true, 'usertest');

-- Inserts Categoria
INSERT INTO Categoria (id, nombre)
VALUES (1, 'SUSPENSO'),
       (2, 'ACCION'),
       (3, 'CIENCIA_FICCION'),
       (4, 'DRAMA'),
       (5, 'CRIMEN');

-- Inserts Pelicula
INSERT INTO Movie (nombre, descripcion, frase, duracion, pais, cantVistas, idCategoria, añoLanzamiento, imagen,
                   idComentario, likes, valoracion, director, escritor, idioma, tambienConocidaComo)
VALUES ('Volver al Futuro',
        'Un adolescente viaja accidentalmente al pasado en un DeLorean y debe asegurarse de que sus padres se enamoren para salvar su propia existencia.',
        '¿Carretera? A donde vamos no necesitamos carreteras.', 116.0, 'EE.UU.', 4500000, 3, '1985',
        'volver_al_futuro.jpg', 2, 50000, 9.0, 'Robert Zemeckis', 'Robert Zemeckis, Bob Gale', 'Inglés',
        'Back to the Future, Regreso al Futuro (España)'),
       ('Apocalypse Now',
        'Durante la guerra de Vietnam, un capitán recibe la misión de eliminar a un coronel rebelde que se ha vuelto loco.',
        'Me encanta el olor a napalm por la mañana.', 153.0, 'EE.UU.', 5000000, 2, '1979', 'apocalypse_now.jpg', 1,
        42000, 8.4, 'Francis Ford Coppola', 'John Milius, Francis Ford Coppola', 'Inglés', 'Apocalypse Now'),
       ('El Bueno, el Malo y el Feo', 'Tres hombres luchan por un tesoro durante la Guerra Civil estadounidense.',
        'Cuando se dispara, se dispara. No se habla.', 178.0, 'Italia', 6000000, 5, '1966',
        'el_bueno_el_malo_y_el_feo.jpg', 2, 51000, 7.9, 'Sergio Leone', 'Sergio Leone, Luciano Vincenzoni', 'Italiano',
        'The Good, the Bad and the Ugly, El Bueno, el Feo y el Malo (España)'),
       ('El Viaje de Chihiro',
        'Una niña entra en un mundo de espíritus, donde debe encontrar el coraje para salvar a sus padres.',
        'Nada de lo que sucede se olvida jamás, aunque no puedas recordarlo.', 125.0, 'Japón', 5000000, 4, '2001',
        'el_viaje_de_chihiro.jpg', 1, 48000, 8.6, 'Hayao Miyazaki', 'Hayao Miyazaki', 'Japonés',
        'Spirited Away, El Viaje de Chihiro (España)'),
       ('Sin Novedades en el Frente', 'Un joven soldado alemán experimenta el horror de la Primera Guerra Mundial.',
        'La guerra no se trata de quién tiene razón, sino de quién queda.', 148.0, 'Alemania', 3000000, 4, '2022',
        'sin_novedades_en_el_frente.jpg', 1, 35000, 8.3, 'Edward Berger', 'Edward Berger, Lesley Paterson, Ian Stokell',
        'Alemán', 'All Quiet on the Western Front, Sin Novedades en el Frente (España)'),
       ('El Origen', 'Un thriller que desafía la mente sobre la infiltración en los sueños.',
        'Tienes que tener miedo de soñar un poco más grande, querido.', 148.0, 'EE.UU.', 5000000, 1, '2010',
        'el_origen.jpg', 1, 35000, 8.8, 'Christopher Nolan', 'Christopher Nolan', 'Inglés',
        'Inception, Origen (España)'),
       ('El Caballero de la Noche',
        'Cuando el peligroso Joker emerge, Batman debe enfrentar una de las mayores pruebas psicológicas y físicas.',
        '¿Por qué tan serio?', 152.0, 'EE.UU.', 7000000, 2, '2008', 'el_caballero_de_la_noche.jpg', 2, 70, 8.9,
        'Christopher Nolan', 'Jonathan Nolan, Christopher Nolan', 'Inglés',
        'The Dark Knight, El Caballero Oscuro (España)'),
       ('Matrix',
        'Un hacker informático descubre la verdadera naturaleza de la realidad y su papel en la guerra contra sus controladores.',
        'No hay cuchara.', 136.0, 'EE.UU.', 6500000, 3, '1999', 'matrix.jpg', 2, 38000, 8.7,
        'Lana Wachowski, Lilly Wachowski', 'Lana Wachowski, Lilly Wachowski', 'Inglés', 'The Matrix, Matrix (España)');

-- Inserts Comentarios
INSERT INTO Comentario (id, descripcion, likes, valoracion, idMovie, idUsuario)
VALUES (null, 'buenisima', 0, 9, 1, 1),
       (null, 'bastante bien', 0, 8, 3, 1);



-- Inserta registros en UsuarioMovie
-- Nota: Se deben conocer los ids de las peliculas
INSERT INTO UsuarioMovie (usuario_id, pelicula_id, fechaVista, esFavorita)
VALUES (1, 1, '2023-10-10', true),  -- Usuario 1 ve "Volver al Futuro"
       (1, 2, '2023-10-11', false), -- Usuario 1 ve "Apocalypse Now"
       (1, 3, '2023-10-12', true),  -- Usuario 1 ve "El Bueno, el Malo y el Feo"
       (1, 4, '2023-10-13', false), -- Usuario 1 ve "El Viaje de Chihiro"
       (1, 5, '2023-10-14', true),  -- Usuario 1 ve "Sin Novedades en el Frente"
       (1, 6, '2023-10-15', false), -- Usuario 1 ve "El Origen"
       (1, 7, '2023-10-16', true),  -- Usuario 1 ve "El Caballero de la Noche"
       (1, 8, '2023-10-17', false); -- Usuario 1 ve "Matrix"