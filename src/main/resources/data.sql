INSERT INTO Pais (id, nombre)
VALUES (1, 'Argentina'),
       (2, 'EE.UU.'),
       (3, 'Corea del Sur'),
       (4, 'Alemania'),
       (5, 'Japón'),
       (6, 'Italia');


-- Inserts del Usuario

INSERT INTO Usuario (email, password, nombre, username, foto_de_perfil, pais_id, descripcion, rol, activo)
VALUES ('admin@example.com', '$2a$10$TVwK/Y5fxZ0jlSdyA2tCieyNQB79HjMzyc6PT3qQHhiOSV5BeIBVe', 'Admin User', 'admin',
        'admin.jpg', 1, 'Administrador del sistema', 'ADMIN', TRUE),
       ('juan.perez@example.com', '$2a$10$TVwK/Y5fxZ0jlSdyA2tCieyNQB79HjMzyc6PT3qQHhiOSV5BeIBVe', 'Juan Perez',
        'juanperez', 'juan.jpg', 2, 'Cinéfilo amateur', 'USER', FALSE),
       ('moderador@example.com', '$2a$10$TVwK/Y5fxZ0jlSdyA2tCieyNQB79HjMzyc6PT3qQHhiOSV5BeIBVe', 'Moderador Uno',
        'moduser', 'moderador.jpg', 4, 'Encargado de moderar reseñas', 'MOD', TRUE),
       ('ana.gomez@example.com', '$2a$10$TVwK/Y5fxZ0jlSdyA2tCieyNQB79HjMzyc6PT3qQHhiOSV5BeIBVe', 'Ana Gomez',
        'anagomez', 'ana.jpg', 3, 'Amante de películas de acción', 'USER', TRUE),
       ('carlos.lopez@example.com', '$2a$10$TVwK/Y5fxZ0jlSdyA2tCieyNQB79HjMzyc6PT3qQHhiOSV5BeIBVe', 'Carlos Lopez',
        'carlosl', 'carlos.jpg', 5, 'Fanático del cine clásico', 'USER', FALSE),
       ('mora@gmail.com', '$2a$10$TVwK/Y5fxZ0jlSdyA2tCieyNQB79HjMzyc6PT3qQHhiOSV5BeIBVe', 'Mora', 'moras', 'mora.jpg',
        5, 'user para pruebas', 'USER', TRUE),
       ('morae2e@gmail.com', '$2a$10$TVwK/Y5fxZ0jlSdyA2tCieyNQB79HjMzyc6PT3qQHhiOSV5BeIBVe', 'Mora E2E', 'morae2e',
        'morae2e.jpg', 5, 'user para pruebas e2e', 'USER', TRUE),
       ('flor@unlam.edu.ar', '$2a$10$BqkXqgYnG35/LujcXESGP.QQOhecbLVcBe6fJcG3CZ62SKjUi5.3e', 'Florencia', 'flor',
        'flor.jpg', 1, 'hola', 'USER', TRUE);

INSERT INTO Seguidor(usuario_id, seguido_id)
VALUES (6, 5),
       (5, 6),
       (5, 8),
       (8, 5),
       (2, 8),
       (8, 2),
       (1, 2),
       (1, 3),
       (1, 4),
       (2, 3),
       (2, 6),
       (3, 4),
       (3, 7),
       (4, 5),
       (4, 8),
       (5, 7),
       (6, 7),
       (6, 8),
       (7, 1),
       (7, 3),
       (8, 1),
       (8, 4);

INSERT INTO Categoria (id, nombre)
VALUES (1, 'SUSPENSO'),
       (2, 'ACCIÓN'),
       (3, 'CIENCIA FICCIÓN'),
       (4, 'DRAMA'),
       (5, 'CRIMEN'),
       (6, 'COMEDIA'),
       (7, 'GUERRA'),
       (8, 'ANIMACIÓN');


INSERT INTO Movie (nombre, descripcion, frase, duracion, pais, cantVistas, añoLanzamiento, fechaLanzamiento, imagen,
                   idComentario,
                   valoracion, director, escritor, idioma, tambienConocidaComo)

VALUES ('Volver al Futuro',
        'Un adolescente viaja accidentalmente al pasado en un DeLorean y debe asegurarse de que sus padres se enamoren para salvar su propia existencia.',
        '¿Carretera? A donde vamos no necesitamos carreteras.', 116.0, 2, 4500000, '1985', '1985-07-03',
        'volver_al_futuro.jpg', 2, 9.0, 'Robert Zemeckis', 'Robert Zemeckis, Bob Gale', 'Inglés',
        'Back to the Future, Regreso al Futuro (España)'),

       ('Apocalypse Now',
        'Durante la guerra de Vietnam, un capitán recibe la misión de eliminar a un coronel rebelde que se ha vuelto loco.',
        'Me encanta el olor a napalm por la mañana.', 153.0, 2, 5000000, '1979', '1979-08-15',
        'apocalypse_now.jpg', 1, 8.4, 'Francis Ford Coppola', 'John Milius, Francis Ford Coppola', 'Inglés',
        'Apocalypse Now'),

       ('El Bueno, el Malo y el Feo',
        'Tres hombres luchan por un tesoro durante la Guerra Civil estadounidense.',
        'Cuando se dispara, se dispara. No se habla.', 178.0, 6, 6000000, '1966', '1966-12-23',
        'el_bueno_el_malo_y_el_feo.jpg', 2, 7.9, 'Sergio Leone', 'Sergio Leone, Luciano Vincenzoni', 'Italiano',
        'The Good, the Bad and the Ugly, El Bueno, el Feo y el Malo (España)'),

       ('El Viaje de Chihiro',
        'Una niña entra en un mundo de espíritus, donde debe encontrar el coraje para salvar a sus padres.',
        'Nada de lo que sucede se olvida jamás, aunque no puedas recordarlo.', 125.0, 5, 5000000, '2001', '2001-07-20',
        'el_viaje_de_chihiro.jpg', 1, 8.6, 'Hayao Miyazaki', 'Hayao Miyazaki', 'Japonés',
        'Spirited Away, El Viaje de Chihiro (España)'),

       ('Sin Novedades en el Frente',
        'Un joven soldado alemán experimenta el horror de la Primera Guerra Mundial.',
        'La guerra no se trata de quién tiene razón, sino de quién queda.', 148.0, 4, 3000000, '2022', '2022-10-28',
        'sin_novedades_en_el_frente.jpg', 1, 8.3, 'Edward Berger', 'Edward Berger, Lesley Paterson, Ian Stokell',
        'Alemán',
        'All Quiet on the Western Front, Sin Novedades en el Frente (España)'),

       ('El Origen',
        'Un thriller que desafía la mente sobre la infiltración en los sueños.',
        'Tienes que tener miedo de soñar un poco más grande, querido.', 148.0, 2, 5000000, '2010', '2010-07-16',
        'el_origen.jpg', 1, 8.8, 'Christopher Nolan', 'Christopher Nolan', 'Inglés',
        'Inception, Origen (España)'),

       ('El Caballero de la Noche',
        'Cuando el Joker emerge, Batman debe enfrentar una de las mayores pruebas psicológicas y físicas.',
        '¿Por qué tan serio?', 152.0, 2, 7000000, '2008', '2008-07-18',
        'el_caballero_de_la_noche.jpg', 2, 8.9, 'Christopher Nolan', 'Jonathan Nolan, Christopher Nolan', 'Inglés',
        'The Dark Knight, El Caballero Oscuro (España)'),

       ('Matrix',
        'Un hacker informático descubre la verdadera naturaleza de la realidad y su papel en la guerra contra sus controladores.',
        'No hay cuchara.', 136.0, 2, 6500000, '1999', '1999-03-31',
        'matrix.jpg', 2, 8.7, 'Lana Wachowski, Lilly Wachowski', 'Lana Wachowski, Lilly Wachowski', 'Inglés',
        'The Matrix, Matrix (España)'),

       ('Interestelar',
        'Un equipo de exploradores viaja a través de un agujero de gusano en el espacio para asegurar la supervivencia de la humanidad.',
        'La humanidad nació en la Tierra. Nunca estuvo destinada a morir aquí.', 169.0, 2, 5500000, '2014',
        '2014-11-07',
        'interestelar.jpg', NULL, 8.6, 'Christopher Nolan', 'Jonathan Nolan, Christopher Nolan', 'Inglés',
        'Interstellar, Interestelar (España)'),

       ('Parásitos',
        'La codicia y la discriminación de clases amenazan la relación simbiótica entre la rica familia Park y el empobrecido clan Kim.',
        'Actúa como si fueras dueño del lugar.', 132.0, 3, 4500000, '2019', '2019-05-30',
        'parasitos.jpg', NULL, 8.6, 'Bong Joon-ho', 'Bong Joon-ho, Han Jin-won', 'Coreano',
        'Parasite, Parásitos (España)'),

       ('Tiempos Violentos',
        'Las vidas de dos sicarios, un boxeador, un gánster y su esposa se entrelazan en cuatro historias de violencia y redención.',
        'Inglés, hijo de p***, ¿lo hablas?', 154.0, 2, 6000000, '1994', '1994-10-14',
        'tiempos_violentos.jpg', NULL, 8.9, 'Quentin Tarantino', 'Quentin Tarantino', 'Inglés',
        'Pulp Fiction, Tiempos Violentos (España)'),

       ('Un Completo Desconocido',
        'Un joven Bob Dylan sacude la escena de la música folk cuando conecta su guitarra eléctrica en el Festival de Folk de Newport en 1965.',
        '', 120.0, 2, 0, '2024', '2024-12-25', 'un_completo_desconocido.jpg', NULL,
        0.0, 'James Mangold', 'James Mangold', 'Inglés', 'A Complete Unknown'),

       ('El Hombre Lobo',
        'Un hombre busca proteger a su familia de un peligroso depredador.',
        '', 122.0, 2, 0, '2025', '2025-01-15', 'el_hombre_lobo.jpg', NULL,
        0.0, 'Leigh Whannell', 'Rebecca Angelo, Lauren Schuker', 'Inglés', 'Wolf Man'),

       ('Mickey 17',
        'Para evitar que Mickey8, su clon sustituto, ocupe su lugar, Mickey7, un robot llamado "prescindible" es enviado a un planeta frío para colonizarlo.',
        '', 120.0, 3, 0, '2025', '2025-04-18', 'mickey17.jpg', NULL,
        0.0, 'Bong Joon Ho', 'Bong Joon Ho, Edward Ashton', 'Inglés', 'Mickey 17'),

       ('F1',
        'La leyenda de las carreras, Sonny Hayes, es persuadido para salir de su retiro y liderar a un equipo de Fórmula 1 en crisis, mientras entrena a un joven piloto prometedor y busca una última oportunidad de gloria.',
        '', 120.0, 3, 0, '2025', '2025-06-25', 'f1.jpg', NULL,
        0.0, 'Joseph Kosinski', 'Ehren Kruger', 'Inglés', 'F1');



-- Inserta registros en UsuarioMovie
-- Nota: Se deben conocer los ids de las peliculas
INSERT INTO UsuarioMovie (usuario_id, pelicula_id, fechaLike, fechaVista, esLike, vistaPorUsuario, esVerMasTarde)
VALUES (1, 1, '2023-10-10', '2023-10-10', true, true, false),  -- Usuario 1 ve "Volver al Futuro"
       (1, 2, '2023-10-11', '2023-10-10', true, true, false),  -- Usuario 1 ve "Apocalypse Now"
       (1, 3, '2023-10-12', '2023-10-10', false, true, false), -- Usuario 1 ve "El Bueno, el Malo y el Feo"
       (1, 4, '2023-10-13', '2023-10-10', true, true, false),  -- Usuario 1 ve "El Viaje de Chihiro"
       (1, 5, '2023-10-14', '2023-10-10', true, true, false),  -- Usuario 1 ve "Sin Novedades en el Frente"
       (1, 6, '2023-10-15', '2023-10-10', true, true, false),  -- Usuario 1 ve "El Origen"
       (1, 7, '2023-10-16', '2023-10-09', true, true, false),  -- Usuario 1 ve "El Caballero de la Noche"
       (6, 1, '2020-07-17', '2024-05-10', true, true, false),
       (6, 2, '2023-10-01', '2024-03-29', true, true, false),
       (6, 3, '2024-03-1', '2024-10-21', false, true, false),
       (6, 4, '2024-04-18', '2023-11-22', false, false, true),
       (6, 5, '2024-01-22', '2023-12-04', true, true, false),
       (6, 6, null, '2023-01-10', true, true, false),
       (6, 7, '2023-01-14', null, true, true, false);

-- Inserts Comentarios
INSERT INTO Comentario (id, descripcion, likes, valoracion, idMovie, idUsuario)
VALUES (null, 'buenisima', 1, 9, 1, 1),
       (null, 'bastante bien', 7, 8, 7, 1),
       (null, 'esta peli la verdad me cambio la vida', 2, 8, 9, 2),
       (null, 'HORRIBLE NO LA VEAN', 13, 1, 11, 3),
       (null, 'Muy entretenida, la recomiendo', 5, 8, 3, 1),
       (null, 'No es mi tipo de película, pero no está mal', 3, 6, 2, 2),
       (null, 'El final es increíble, me encantó', 10, 9, 4, 3),
       (null, 'Un clásico, siempre es bueno volver a verla', 8, 10, 1, 4),
       (null, 'La actuación principal es impecable', 4, 7, 5, 5),
       (null, 'La trama es muy predecible, no me gustó', 1, 3, 6, 6),
       (null, 'Una obra maestra, sin duda', 12, 10, 7, 7),
       (null, 'Buena, pero esperaba más', 6, 6, 8, 8),
       (null, 'Los efectos especiales son increíbles', 9, 8, 9, 1),
       (null, 'Demasiado lenta para mi gusto', 2, 4, 10, 2),
       (null, 'Excelente dirección y guion', 11, 9, 11, 3),
       (null, 'Me aburrió, pero tiene algunos momentos buenos', 3, 5, 12, 4),
       (null, 'Recomendable para ver en familia', 7, 8, 13, 5),
       (null, 'Parece más un documental que una película', 2, 6, 14, 6),
       (null, 'Mi película favorita de este año', 15, 10, 14, 7),
       (null, 'No entiendo cómo tiene tan buenas críticas', 1, 2, 12, 8);

-- Insertar likes en la tabla UsuarioComentario
INSERT INTO UsuarioComentario (comentario_id, usuario_id, likeComentario)
VALUES (1, 6, TRUE),   -- El usuario 6 da like al comentario 1
       (2, 1, TRUE),   -- El usuario 1 da like al comentario 2
       (3, 2, FALSE),  -- El usuario 2 no da like al comentario 3
       (4, 3, TRUE),   -- El usuario 3 da like al comentario 4
       (5, 4, TRUE),   -- El usuario 4 da like al comentario 5
       (6, 5, FALSE),  -- El usuario 5 no da like al comentario 6
       (7, 6, TRUE),   -- El usuario 6 da like al comentario 7
       (8, 7, TRUE),   -- El usuario 7 da like al comentario 8
       (9, 8, FALSE),  -- El usuario 8 no da like al comentario 9
       (10, 1, TRUE),  -- El usuario 1 da like al comentario 10
       (11, 2, TRUE),  -- El usuario 2 da like al comentario 11
       (12, 3, FALSE), -- El usuario 3 no da like al comentario 12
       (13, 4, TRUE),  -- El usuario 4 da like al comentario 13
       (14, 5, TRUE),  -- El usuario 5 da like al comentario 14
       (15, 6, FALSE), -- El usuario 6 no da like al comentario 15
       (16, 7, TRUE); -- El usuario 7 da like al comentario 16

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
       (11, 5),
       (12, 4),
       (13, 1),
       (14, 3),
       (14, 6);

INSERT INTO ListaColaborativa (nombre, creador_id, colaborador_id, fechaCreacion)
VALUES ('Películas favoritas de acción', 1, 2, '2024-01-01'),
       ('Comedias para el fin de semana', 3, 4, '2024-02-10'),
       ('Clásicos imperdibles', 5, 6, '2024-03-15'),
       ('Documentales recomendados', 7, 8, '2024-04-20'),
       ('Películas románticas', 2, 3, '2024-05-05');

INSERT INTO ListaMovie (lista_id, movie_id, usuario_id, fechaAgregada)
VALUES (1, 1, 1, '2024-01-02'),  -- Usuario 1 agrega película 1 a la lista 1
       (1, 2, 2, '2024-01-03'),  -- Usuario 2 agrega película 2 a la lista 1
       (1, 3, 3, '2024-01-04'),  -- Usuario 3 agrega película 3 a la lista 1
       (2, 4, 4, '2024-02-11'),  -- Usuario 4 agrega película 4 a la lista 2
       (2, 5, 5, '2024-02-12'),  -- Usuario 5 agrega película 5 a la lista 2
       (3, 6, 6, '2024-03-16'),  -- Usuario 6 agrega película 6 a la lista 3
       (3, 7, 7, '2024-03-17'),  -- Usuario 7 agrega película 7 a la lista 3
       (3, 8, 8, '2024-03-18'),  -- Usuario 8 agrega película 8 a la lista 3
       (4, 9, 1, '2024-04-21'),  -- Usuario 1 agrega película 9 a la lista 4
       (4, 10, 2, '2024-04-22'), -- Usuario 2 agrega película 10 a la lista 4
       (4, 11, 3, '2024-04-23'), -- Usuario 3 agrega película 11 a la lista 4
       (5, 12, 4, '2024-05-06'), -- Usuario 4 agrega película 12 a la lista 5
       (5, 13, 5, '2024-05-07'), -- Usuario 5 agrega película 13 a la lista 5
       (5, 14, 6, '2024-05-08'); -- Usuario 6 agrega película 14 a la lista 5