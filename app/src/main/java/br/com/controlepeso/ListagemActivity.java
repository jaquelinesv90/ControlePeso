package br.com.controlepeso;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

import br.com.controlepeso.modelo.Pessoa;
import br.com.controlepeso.persistencia.PessoasDatabase;

public class ListagemActivity extends AppCompatActivity {

    private static final int NOVO    = 1;
    private static final int ALTERAR = 2;

    private ListView listViewDados;
    private ArrayList<Pessoa> pessoas;

    private PessoaAdapter listaAdapter;
    private ArrayList<Pessoa> listaDadosPessoa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_listagem);
        setTitle(getString(R.string.meu_controle_de_peso));

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.listViewDados), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        listViewDados = findViewById(R.id.listViewDados);

        listViewDados.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent,
                                    View view,
                                    int position,
                                    long id) {

                Pessoa pessoa = (Pessoa) listViewDados.getItemAtPosition(position);
            }
        });

        popularLista();
        popularListaDados();
    }
    public static final String DT_AVALIACAO = "DT_AVALIACAO";
    private TextView textViewDados;

    private void popularLista(){
        PessoasDatabase database = PessoasDatabase.getDatabase(this);
        List<Pessoa> lista = database.pessoaDao().queryAll();

        listaAdapter = new PessoaAdapter(this,
                                        lista);
        listViewDados.setAdapter(listaAdapter);
    }

    public void popularListaDados(){
        PessoasDatabase database = PessoasDatabase.getDatabase(this);

        List<Pessoa> lista = database.pessoaDao().queryAll();

        listaAdapter = new PessoaAdapter(this,
                lista);

        listViewDados.setAdapter(listaAdapter);
    }

    @Override
    public  boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.listagem_opcoes,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int menuSelecionado = item.getItemId();

        if(menuSelecionado == R.id.menuItemAdicionar){
           // adicionar(item.getActionView());
            CadastroActivity.nova(this, NOVO);

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static void novoDadosPessoa(AppCompatActivity activity,
                                       ActivityResultLauncher<Intent> launcher){

        Intent intent = new Intent(activity,CadastroActivity.class);
        launcher.launch(intent);
    }

    ActivityResultLauncher<Intent> launcherNovoDadosPessoa =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult result) {
                            if(result.getResultCode() == Activity.RESULT_OK){
                                Intent intent = result.getData();
                                Bundle bundle = intent.getExtras();

                                if(bundle != null){
                                    String dataNascimento = bundle.getString(CadastroActivity.DT_NASCIMENTO);
                                    String altura = bundle.getString(CadastroActivity.ALTURA);
                                    String peso = bundle.getString(CadastroActivity.PESO);
                                    String genero = bundle.getString(CadastroActivity.GENERO);
                                    String dataAvaliacao = bundle.getString(CadastroActivity.DT_AVALIACAO);
                                    String objetivo = bundle.getString(CadastroActivity.OBJETIVO);
                                    String imc = bundle.getString(CadastroActivity.IMC);
                                    String ida = bundle.getString(CadastroActivity.IDA);

                                    Pessoa pessoa = new Pessoa();
                                    listaDadosPessoa.add(pessoa);
                                    listaAdapter.notifyDataSetChanged();
                                }
                            }
                        }
                    });

    //novaPessoa metodo
    public void novoDadoPessoa(View view){
        ListagemActivity.novoDadosPessoa(this,launcherNovoDadosPessoa);
    }

}