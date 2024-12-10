package br.com.controlepeso.persistencia;

import br.com.controlepeso.modelo.Pessoa;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Pessoa.class},version = 1, exportSchema = false)
public abstract class PessoasDatabase extends RoomDatabase {

    public abstract PessoaDao pessoaDao();

    private static PessoasDatabase instance;

    public static PessoasDatabase getDatabase(final Context context){
        if(instance == null){
            synchronized (PessoasDatabase.class){
                if(instance == null){
                    instance = Room.databaseBuilder(context,
                            PessoasDatabase.class,
                            "pessoas.db").allowMainThreadQueries().build();
                }
            }
        }
        return instance;
    }
}
