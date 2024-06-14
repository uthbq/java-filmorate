package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UpdateException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmMpa;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@Slf4j
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Film create(Film film) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName("films")
                .usingGeneratedKeyColumns("id");
        Integer newId = simpleJdbcInsert.executeAndReturnKey(film.toMap()).intValue();
        log.info("Фильму " + film.getName() + " присвоен id: " + newId);
        film.setId(newId);
        return film;
    }


    @Override
    public Film update(Film film) {
        String sqlQuery = "UPDATE FILMS SET MPA_ID=?, DESCRIPTION=?, " +
                "RELEASE_DATE=?, DURATION=?, NAME=? WHERE ID=?;";
        jdbcTemplate.update(sqlQuery, film.getMpa().getId(), film.getDescription(), film.getReleaseDate(),
                film.getDuration(), film.getName(), film.getId());
        return getFilmById(film.getId());
    }

    @Override
    public List<Film> getAll() {
        String sqlQuery = "SELECT * FROM films";
        return jdbcTemplate.query(sqlQuery, this::mapRowToFilm);
    }

    @Override
    public Film getFilmById(int id) {
        String sqlQuery = "SELECT * FROM films WHERE id = ?";
        return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToFilm, id);
    }

    @Override
    public void putGenreIdAndFilmId(int filmId, int genreId) {
        String sqlQuery = "INSERT INTO film_genres(film_id, genre_id) VALUES (?, ?)";
        jdbcTemplate.update(sqlQuery, filmId, genreId);
    }

    @Override
    public FilmMpa getMpaById(int id) {
        String sqlQuery = "SELECT * FROM mpa WHERE id = ?";
        return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToFilmMpa, id);
    }

    @Override
    public List<FilmMpa> getAllMpas() {
        String sqlQuery = "SELECT * FROM mpa;";
        return jdbcTemplate.query(sqlQuery, this::mapRowToFilmMpa);
    }

    @Override
    public List<Genre> getAllGenres() {
        String sqlQuery = "SELECT * FROM genres;";
        return jdbcTemplate.query(sqlQuery, this::mapRowToGenre);
    }

    @Override
    public Genre getGenreById(int id) {
        String sqlQuery = "SELECT * FROM genres WHERE id = ?";
        return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToGenre, id);
    }

    @Override
    public Set<Genre> getAllGenresByFilmId(int filmId) {
        String sqlQuery = "SELECT fg.GENRE_ID AS id, g.NAME AS name \n " +
                "FROM FILM_GENRES AS fg\n" +
                "INNER JOIN GENRES AS g ON fg.GENRE_ID  = g.ID\n" +
                "WHERE fg.FILM_ID = ?\n" +
                "ORDER BY fg.GENRE_ID;";
        return new HashSet<>(jdbcTemplate.query(sqlQuery, this::mapRowToGenre, filmId));
    }

    @Override
    public Film addLikeToFilm(int userId, int filmId) {
        String sqlQuery = "INSERT INTO film_likes (USER_ID, FILM_ID) VALUES (?, ?)";
        jdbcTemplate.update(sqlQuery, userId, filmId);
        return getFilmById(filmId);
    }

    @Override
    public Film removeLikeFromFilm(int userId, int filmId) {
        String sqlQuery = "DELETE FROM film_likes WHERE film_id = ? AND user_id = ?";
        jdbcTemplate.update(sqlQuery, filmId, userId);
        return getFilmById(filmId);
    }

    @Override
    public Set<Integer> getAllFilmLikes(int filmId) {
        Set<Integer> result = new HashSet<>();
        String sqlQuery = "SELECT DISTINCT user_id FROM film_likes WHERE FILM_ID = ?";
        jdbcTemplate.query(sqlQuery, rs -> {
            result.add(rs.getInt("user_id"));
        }, filmId);
        return result;
    }

    @Override
    public void deleteFilm(int id) {
        String sqlQuery = "DELETE FROM films WHERE id = ?";
        int update = jdbcTemplate.update(sqlQuery,id);
        if (update == 0) {
            throw new UpdateException("Фильм с id =  " + id + " не найден");
        }
    }

    @Override
    public List<Film> getCommonFilms(int userId, int friendId) {
        String sqlQuery = " SELECT f.ID, f.NAME, f.DESCRIPTION, f.RELEASE_DATE, f.DURATION," +
                "f.MPA_ID, m.NAME AS mpa_name" +
                " FROM films AS f " +
                "JOIN MPA AS m ON m.ID = f.MPA_ID " +
                "JOIN FILM_LIKES AS l ON f.ID = l.FILM_ID " +
                "JOIN FILM_LIKES AS lf ON l.FILM_ID = lf.FILM_ID " +
                "WHERE l.USER_ID = ? and lf.USER_ID = ?";

        return jdbcTemplate.query(sqlQuery, this::mapRowToFilmWithGenres, userId, friendId);

    }

    private FilmMpa mapRowToFilmMpa(ResultSet rs, int rowNum) throws SQLException {
        FilmMpa filmMpa = new FilmMpa();
        filmMpa.setId(rs.getInt("id"));
        filmMpa.setName(rs.getString("name"));
        return filmMpa;
    }

    private Genre mapRowToGenre(ResultSet rs, int rowNum) throws SQLException {
        Genre genre = new Genre();
        genre.setId(rs.getInt("id"));
        genre.setName(rs.getString("name"));
        return genre;
    }

    private Film mapRowToFilm(ResultSet rs, int rowNum) throws SQLException {
        Film film = new Film();
        film.setId(rs.getInt("id"));
        film.setName(rs.getString("name"));
        film.setDescription(rs.getString("description"));
        film.setMpa(getMpaById(rs.getInt("mpa_id")));
        film.setReleaseDate(rs.getDate("release_date").toLocalDate());
        film.setDuration(rs.getInt("duration"));
        return film;
    }
    private Film mapRowToFilmWithGenres(ResultSet rs, int rowNum) throws SQLException {
        Film film = mapRowToFilm(rs,rowNum);
        film.setGenres(getAllGenresByFilmId(rs.getInt("id")));
        return film;
    }




}
