--INSERT INTO Usuario(id, email, password, rol, activo)
--VALUES (null, 'test@unlam.edu.ar', 'test', 'ADMIN', true);
-- Volver al Futuro
INSERT INTO
        Movie (
                nombre,
                descripcion,
                frase,
                duracion,
                pais,
                cantVistas,
                idCategoria,
                añoLanzamiento,
                imagen,
                idComentario,
                likes,
                valoracion,
                director,
                escritor,
                idioma,
                tambienConocidaComo
        )
VALUES
        (
                'Volver al Futuro',
                'Un adolescente viaja accidentalmente al pasado en un DeLorean y debe asegurarse de que sus padres se enamoren para salvar su propia existencia.',
                '¿Carretera? A donde vamos no necesitamos carreteras.',
                116.0,
                'EE.UU.',
                4500000,
                3,
                -- CIENCIA_FICCION
                '1985',
                'volver_al_futuro.jpg',
                null,
                50000,
                9.0,
                'Robert Zemeckis',
                'Robert Zemeckis, Bob Gale',
                'Inglés',
                'Back to the Future, Regreso al Futuro (España)'
        );

-- Apocalypse Now
INSERT INTO
        Movie (
                nombre,
                descripcion,
                frase,
                duracion,
                pais,
                cantVistas,
                idCategoria,
                añoLanzamiento,
                imagen,
                idComentario,
                likes,
                valoracion,
                director,
                escritor,
                idioma,
                tambienConocidaComo
        )
VALUES
        (
                'Apocalypse Now',
                'Durante la guerra de Vietnam, un capitán recibe la misión de eliminar a un coronel rebelde que se ha vuelto loco.',
                'Me encanta el olor a napalm por la mañana.',
                153.0,
                'EE.UU.',
                5000000,
                2,
                -- ACCION
                '1979',
                'apocalypse_now.jpg',
                null,
                42000,
                8.4,
                'Francis Ford Coppola',
                'John Milius, Francis Ford Coppola',
                'Inglés',
                'Apocalypse Now'
        );

-- El Bueno, el Malo y el Feo
INSERT INTO
        Movie (
                nombre,
                descripcion,
                frase,
                duracion,
                pais,
                cantVistas,
                idCategoria,
                añoLanzamiento,
                imagen,
                idComentario,
                likes,
                valoracion,
                director,
                escritor,
                idioma,
                tambienConocidaComo
        )
VALUES
        (
                'El Bueno, el Malo y el Feo',
                'Tres hombres luchan por un tesoro durante la Guerra Civil estadounidense.',
                'Cuando se dispara, se dispara. No se habla.',
                178.0,
                'Italia',
                6000000,
                5,
                -- CRIMEN
                '1966',
                'el_bueno_el_malo_y_el_feo.jpg',
                null,
                51000,
                7.9,
                'Sergio Leone',
                'Sergio Leone, Luciano Vincenzoni',
                'Italiano',
                'The Good, the Bad and the Ugly, El Bueno, el Feo y el Malo (España)'
        );

-- El Viaje de Chihiro
INSERT INTO
        Movie (
                nombre,
                descripcion,
                frase,
                duracion,
                pais,
                cantVistas,
                idCategoria,
                añoLanzamiento,
                imagen,
                idComentario,
                likes,
                valoracion,
                director,
                escritor,
                idioma,
                tambienConocidaComo
        )
VALUES
        (
                'El Viaje de Chihiro',
                'Una niña entra en un mundo de espíritus, donde debe encontrar el coraje para salvar a sus padres.',
                'Nada de lo que sucede se olvida jamás, aunque no puedas recordarlo.',
                125.0,
                'Japón',
                5000000,
                4,
                -- DRAMA
                '2001',
                'el_viaje_de_chihiro.jpg',
                null,
                48000,
                8.6,
                'Hayao Miyazaki',
                'Hayao Miyazaki',
                'Japonés',
                'Spirited Away, El Viaje de Chihiro (España)'
        );

-- Sin Novedades en el Frente
INSERT INTO
        Movie (
                nombre,
                descripcion,
                frase,
                duracion,
                pais,
                cantVistas,
                idCategoria,
                añoLanzamiento,
                imagen,
                idComentario,
                likes,
                valoracion,
                director,
                escritor,
                idioma,
                tambienConocidaComo
        )
VALUES
        (
                'Sin Novedades en el Frente',
                'Un joven soldado alemán experimenta el horror de la Primera Guerra Mundial.',
                'La guerra no se trata de quién tiene razón, sino de quién queda.',
                148.0,
                'Alemania',
                3000000,
                4,
                -- DRAMA
                '2022',
                'sin_novedades_en_el_frente.jpg',
                null,
                35000,
                8.3,
                'Edward Berger',
                'Edward Berger, Lesley Paterson, Ian Stokell',
                'Alemán',
                'All Quiet on the Western Front, Sin Novedades en el Frente (España)'
        );

-- El Origen
INSERT INTO
        Movie (
                nombre,
                descripcion,
                frase,
                duracion,
                pais,
                cantVistas,
                idCategoria,
                añoLanzamiento,
                imagen,
                idComentario,
                likes,
                valoracion,
                director,
                escritor,
                idioma,
                tambienConocidaComo
        )
VALUES
        (
                'El Origen',
                'Un thriller que desafía la mente sobre la infiltración en los sueños.',
                'Tienes que tener miedo de soñar un poco más grande, querido.',
                148.0,
                'EE.UU.',
                5000000,
                1,
                -- SUSPENSO
                '2010',
                'el_origen.jpg',
                1,
                35000,
                8.8,
                'Christopher Nolan',
                'Christopher Nolan',
                'Inglés',
                'Inception, Origen (España)'
        );

-- El Caballero de la Noche
INSERT INTO
        Movie (
                nombre,
                descripcion,
                frase,
                duracion,
                pais,
                cantVistas,
                idCategoria,
                añoLanzamiento,
                imagen,
                idComentario,
                likes,
                valoracion,
                director,
                escritor,
                idioma,
                tambienConocidaComo
        )
VALUES
        (
                'El Caballero de la Noche',
                'Cuando el peligroso Joker emerge, Batman debe enfrentar una de las mayores pruebas psicológicas y físicas.',
                '¿Por qué tan serio?',
                152.0,
                'EE.UU.',
                7000000,
                2,
                -- ACCION
                '2008',
                'el_caballero_de_la_noche.jpg',
                2,
                70,
                8.9,
                'Christopher Nolan',
                'Jonathan Nolan, Christopher Nolan',
                'Inglés',
                'The Dark Knight, El Caballero Oscuro (España)'
        );

-- Matrix
INSERT INTO
        Movie (
                nombre,
                descripcion,
                frase,
                duracion,
                pais,
                cantVistas,
                idCategoria,
                añoLanzamiento,
                imagen,
                idComentario,
                likes,
                valoracion,
                director,
                escritor,
                idioma,
                tambienConocidaComo
        )
VALUES
        (
                'Matrix',
                'Un hacker informático descubre la verdadera naturaleza de la realidad y su papel en la guerra contra sus controladores.',
                'No hay cuchara.',
                136.0,
                'EE.UU.',
                6500000,
                3,
                -- CIENCIA_FICCION
                '1999',
                'matrix.jpg',
                2,
                38000,
                8.7,
                'Lana Wachowski, Lilly Wachowski',
                'Lana Wachowski, Lilly Wachowski',
                'Inglés',
                'The Matrix, Matrix (España)'
        );

-- Interestelar
INSERT INTO
        Movie (
                nombre,
                descripcion,
                frase,
                duracion,
                pais,
                cantVistas,
                idCategoria,
                añoLanzamiento,
                imagen,
                idComentario,
                likes,
                valoracion,
                director,
                escritor,
                idioma,
                tambienConocidaComo
        )
VALUES
        (
                'Interestelar',
                'Un equipo de exploradores viaja a través de un agujero de gusano en el espacio para asegurar la supervivencia de la humanidad.',
                'La humanidad nació en la Tierra. Nunca estuvo destinada a morir aquí.',
                169.0,
                'EE.UU.',
                5500000,
                3,
                -- CIENCIA_FICCION
                '2014',
                'interestelar.jpg',
                null,
                42000,
                8.6,
                'Christopher Nolan',
                'Jonathan Nolan, Christopher Nolan',
                'Inglés',
                'Interstellar, Interestelar (España)'
        );

-- Parásitos
INSERT INTO
        Movie (
                nombre,
                descripcion,
                frase,
                duracion,
                pais,
                cantVistas,
                idCategoria,
                añoLanzamiento,
                imagen,
                idComentario,
                likes,
                valoracion,
                director,
                escritor,
                idioma,
                tambienConocidaComo
        )
VALUES
        (
                'Parásitos',
                'La codicia y la discriminación de clases amenazan la relación simbiótica entre la rica familia Park y el empobrecido clan Kim.',
                'Actúa como si fueras dueño del lugar.',
                132.0,
                'Corea del Sur',
                4500000,
                4,
                -- DRAMA
                '2019',
                'parasitos.jpg',
                null,
                40000,
                8.6,
                'Bong Joon-ho',
                'Bong Joon-ho, Han Jin-won',
                'Coreano',
                'Parasite, Parásitos (España)'
        );

-- Tiempos Violentos
INSERT INTO
        Movie (
                nombre,
                descripcion,
                frase,
                duracion,
                pais,
                cantVistas,
                idCategoria,
                añoLanzamiento,
                imagen,
                idComentario,
                likes,
                valoracion,
                director,
                escritor,
                idioma,
                tambienConocidaComo
        )
VALUES
        (
                'Tiempos Violentos',
                'Las vidas de dos sicarios, un boxeador, un gánster y su esposa se entrelazan en cuatro historias de violencia y redención.',
                'Inglés, hijo de p***, ¿lo hablas?',
                154.0,
                'EE.UU.',
                6000000,
                5,
                -- CRIMEN
                '1994',
                'tiempos_violentos.jpg',
                null,
                50000,
                8.9,
                'Quentin Tarantino',
                'Quentin Tarantino',
                'Inglés',
                'Pulp Fiction, Tiempos Violentos (España)'
        );

INSERT INTO
        Categoria (id, nombre)
VALUES
        (1, 'SUSPENSO'),
        (2, 'ACCION'),
        (3, 'CIENCIA_FICCION'),
        (4, 'DRAMA'),
        (5, 'CRIMEN');

INSERT INTO
        Comentario (id, idMovie, descripcion, idUsuario, likes)
VALUES
        (null, 1, 'horrible pelicula', 1, 1),
        (null, 1, 'muy buena', 2, 9);


INSERT INTO usuario (activo, descripcion, email, id, nombre, pais, password, rol, username) VALUES
(true, 'Amante del cine de acción y aventura', 'jose@example.com', 1, 'Jose', 'Argentina', 'hashed_password_1', 'usuario', '@Joseee123'),
(true, 'Fan de las películas de terror y suspenso', 'maria@example.com', 2, 'Maria', 'España', 'hashed_password_2', 'usuario', '@Maria456'),
(true, 'Cineasta en formación, adicto a las comedias', 'luis@example.com', 3, 'Luis', 'México', 'hashed_password_3', 'usuario', '@LuisCine'),
(true, 'Crítico de cine, especializado en dramas', 'ana@example.com', 4, 'Ana', 'Colombia', 'hashed_password_4', 'usuario', '@AnaCritica'),
(true, 'Entusiasta de las películas de ciencia ficción', 'carla@example.com', 5, 'Carla', 'Chile', 'hashed_password_5', 'usuario', '@CarlaSciFi'),
(true, 'Adicto a las series y películas de fantasía', 'pablo@example.com', 6, 'Pablo', 'Perú', 'hashed_password_6', 'usuario', '@PabloFantasia');

INSERT INTO usuario (activo, descripcion, email, id, nombre, pais, password, rol, username) VALUES
                                                                                                (true, 'Amante del cine de acción y aventura', 'test@unlam.edu.ar', 7, 'Jose', 'Argentina', 'hashed_password_7', 'usuario', '@test');