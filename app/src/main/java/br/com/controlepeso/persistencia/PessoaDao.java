package br.com.controlepeso.persistencia;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import br.com.controlepeso.modelo.Pessoa;

@Dao
public interface PessoaDao {
    @Insert
    long insert(Pessoa pessoa);

    @Delete
    void delete(Pessoa pessoa);

    @Update
    void update(Pessoa pessoa);

    @Query("SELECT * FROM pessoa WHERE id =:id")
    Pessoa queryById(long id);

    @Query("SELECT * FROM pessoa ORDER BY id ASC")
    List<Pessoa> queryAll();

}
