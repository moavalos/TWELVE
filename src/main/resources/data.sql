INSERT INTO Pais (id, nombre)
VALUES (1, 'Argentina'),
       (2, 'EE.UU.'),
       (3, 'Corea del Sur'),
       (4, 'Alemania'),
       (5, 'Japón'),
       (6, 'Italia');


-- Inserts del Usuario

INSERT INTO Usuario (email, password, nombre, username, pais_id, descripcion, rol, activo)
VALUES
    ('admin@example.com', 'password123', 'Admin User', 'admin', 1, 'Administrador del sistema', 'ADMIN', TRUE),
    ('juan.perez@example.com', 'pass1234', 'Juan Perez', 'juanperez', 2, 'Cinéfilo amateur', 'USER', FALSE),
    ('moderador@example.com', 'modpass456', 'Moderador Uno', 'moduser', 4, 'Encargado de moderar reseñas', 'MOD', TRUE),
    ('ana.gomez@example.com', 'ana123', 'Ana Gomez', 'anagomez', 3, 'Amante de películas de acción', 'USER', TRUE),
    ('carlos.lopez@example.com', 'carlospass', 'Carlos Lopez', 'carlosl', 5, 'Fanático del cine clásico', 'USER', FALSE),
    ('mora@gmail.com', '$2a$10$TVwK/Y5fxZ0jlSdyA2tCieyNQB79HjMzyc6PT3qQHhiOSV5BeIBVe', 'Mora', 'moras', 5, 'user para pruebas', 'USER', TRUE),
    ('morae2e@gmail.com', '$2a$10$TVwK/Y5fxZ0jlSdyA2tCieyNQB79HjMzyc6PT3qQHhiOSV5BeIBVe', 'Mora E2E', 'morae2e', 5, 'user para pruebas e2e', 'USER', TRUE);


INSERT INTO Categoria (id, nombre)
VALUES (1, 'SUSPENSO'),
       (2, 'ACCION'),
       (3, 'CIENCIA_FICCION'),
       (4, 'DRAMA'),
       (5, 'CRIMEN'),
       (6, 'COMEDIA'),
       (7, 'GUERRA'),
       (8, 'ANIMACIÓN');




-- Inserts Pelicula sin idCategoria
INSERT INTO Movie (nombre, descripcion, frase, duracion, pais, cantVistas, añoLanzamiento, imagen, idComentario,
                   valoracion, director, escritor, idioma, tambienConocidaComo)

VALUES ('Volver al Futuro',
        'Un adolescente viaja accidentalmente al pasado en un DeLorean y debe asegurarse de que sus padres se enamoren para salvar su propia existencia.',
        '¿Carretera? A donde vamos no necesitamos carreteras.', 116.0, 2, 4500000, '1985',
        'volver_al_futuro.jpg', 2, 9.0, 'Robert Zemeckis', 'Robert Zemeckis, Bob Gale', 'Inglés',
        'Back to the Future, Regreso al Futuro (España)'),
       ('Apocalypse Now',
        'Durante la guerra de Vietnam, un capitán recibe la misión de eliminar a un coronel rebelde que se ha vuelto loco.',
        'Me encanta el olor a napalm por la mañana.', 153.0, 2, 5000000, '1979', 'apocalypse_now.jpg', 1,
        8.4, 'Francis Ford Coppola', 'John Milius, Francis Ford Coppola', 'Inglés', 'Apocalypse Now'),
       ('El Bueno, el Malo y el Feo', 'Tres hombres luchan por un tesoro durante la Guerra Civil estadounidense.',
        'Cuando se dispara, se dispara. No se habla.', 178.0, 6, 6000000, '1966',
        'el_bueno_el_malo_y_el_feo.jpg', 2, 7.9, 'Sergio Leone', 'Sergio Leone, Luciano Vincenzoni', 'Italiano',
        'The Good, the Bad and the Ugly, El Bueno, el Feo y el Malo (España)'),
       ('El Viaje de Chihiro',
        'Una niña entra en un mundo de espíritus, donde debe encontrar el coraje para salvar a sus padres.',
        'Nada de lo que sucede se olvida jamás, aunque no puedas recordarlo.', 125.0, 5, 5000000, '2001',
        'el_viaje_de_chihiro.jpg', 1, 8.6, 'Hayao Miyazaki', 'Hayao Miyazaki', 'Japonés',
        'Spirited Away, El Viaje de Chihiro (España)'),
       ('Sin Novedades en el Frente', 'Un joven soldado alemán experimenta el horror de la Primera Guerra Mundial.',
        'La guerra no se trata de quién tiene razón, sino de quién queda.', 148.0, 4, 3000000, '2022',
        'sin_novedades_en_el_frente.jpg', 1, 8.3, 'Edward Berger', 'Edward Berger, Lesley Paterson, Ian Stokell',
        'Alemán', 'All Quiet on the Western Front, Sin Novedades en el Frente (España)'),
       ('El Origen', 'Un thriller que desafía la mente sobre la infiltración en los sueños.',
        'Tienes que tener miedo de soñar un poco más grande, querido.', 148.0, 2, 5000000, '2010',
        'el_origen.jpg', 1, 8.8, 'Christopher Nolan', 'Christopher Nolan', 'Inglés',
        'Inception, Origen (España)'),
       ('El Caballero de la Noche',
        'Cuando el Joker emerge, Batman debe enfrentar una de las mayores pruebas psicológicas y físicas.',
        '¿Por qué tan serio?', 152.0, 2, 7000000, '2008', 'el_caballero_de_la_noche.jpg', 2, 8.9,
        'Christopher Nolan', 'Jonathan Nolan, Christopher Nolan', 'Inglés',
        'The Dark Knight, El Caballero Oscuro (España)'),
       ('Matrix',
        'Un hacker informático descubre la verdadera naturaleza de la realidad y su papel en la guerra contra sus controladores.',
        'No hay cuchara.', 136.0, 2, 6500000, '1999', 'matrix.jpg', 2, 8.7,
        'Lana Wachowski, Lilly Wachowski', 'Lana Wachowski, Lilly Wachowski', 'Inglés', 'The Matrix, Matrix (España)'),
       ('Interestelar',
        'Un equipo de exploradores viaja a través de un agujero de gusano en el espacio para asegurar la supervivencia de la humanidad.',
        'La humanidad nació en la Tierra. Nunca estuvo destinada a morir aquí.', 169.0, 2, 5500000, '2014',
        'interestelar.jpg', NULL, 8.6, 'Christopher Nolan', 'Jonathan Nolan, Christopher Nolan', 'Inglés',
        'Interstellar, Interestelar (España)'),
       ('Parásitos',
        'La codicia y la discriminación de clases amenazan la relación simbiótica entre la rica familia Park y el empobrecido clan Kim.',
        'Actúa como si fueras dueño del lugar.', 132.0, 3, 4500000, '2019', 'parasitos.jpg', NULL,
        8.6, 'Bong Joon-ho', 'Bong Joon-ho, Han Jin-won', 'Coreano', 'Parasite, Parásitos (España)'),
       ('Tiempos Violentos',
        'Las vidas de dos sicarios, un boxeador, un gánster y su esposa se entrelazan en cuatro historias de violencia y redención.',
        'Inglés, hijo de p***, ¿lo hablas?', 154.0, 2, 6000000, '1994', 'tiempos_violentos.jpg', NULL,
        8.9, 'Quentin Tarantino', 'Quentin Tarantino', 'Inglés', 'Pulp Fiction, Tiempos Violentos (España)');

-- Inserta registros en UsuarioMovie
-- Nota: Se deben conocer los ids de las peliculas
INSERT INTO UsuarioMovie (usuario_id, pelicula_id, fechaLike, esLike)
VALUES (1, 1, '2023-10-10', true),  -- Usuario 1 ve "Volver al Futuro"
       (1, 2, '2023-10-11', true), -- Usuario 1 ve "Apocalypse Now"
       (1, 3, '2023-10-12', true),  -- Usuario 1 ve "El Bueno, el Malo y el Feo"
       (1, 4, '2023-10-13', true), -- Usuario 1 ve "El Viaje de Chihiro"
       (1, 5, '2023-10-14', true),  -- Usuario 1 ve "Sin Novedades en el Frente"
       (1, 6, '2023-10-15', true), -- Usuario 1 ve "El Origen"
       (1, 7, '2023-10-16', true),  -- Usuario 1 ve "El Caballero de la Noche"
       (1, 8, '2023-10-17', true); -- Usuario 1 ve "Matrix"

-- Inserts Comentarios
INSERT INTO Comentario (id, descripcion, likes, valoracion, idMovie, idUsuario)
VALUES (null, 'buenisima', 0, 9, 1, 1),
       (null, 'bastante bien', 0, 8, 7, 1);


INSERT INTO movie_categoria (movie_id, categoria_id)
VALUES (1, 6),
       (1, 3),
       (2, 2),
       (2, 7),
       (3, 5),
       (4, 8),
       (4, 4),
       (5, 2),
       (5, 4),
       (5, 7),
       (6, 1),
       (6, 3),
       (7, 2),
       (7, 5),
       (7, 4),
       (8, 3),
       (9, 4),
       (9, 1),
       (10, 5),
       (10, 1),
       (11, 1),
       (11, 5);
