package br.com.controlepeso;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
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
import androidx.appcompat.view.ActionMode;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

import br.com.controlepeso.modelo.Pessoa;
import br.com.controlepeso.persistencia.PessoasDatabase;

public class ListagemActivity extends AppCompatActivity {

    private ListView listViewDados;
    private PessoaAdapter listaAdapter;
    private ArrayList<Pessoa> listaDadosPessoa;

    private ActionMode actionMode;
    private View viewSelecionada;
    private  int posicaoSelecionada = -1;

    public static final int NOVO = 1;

    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater inflate = mode.getMenuInflater();
            inflate.inflate(R.menu.listagem_item_selecionado,menu);

            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            int idMenuItem = item.getItemId();

            if(idMenuItem ==R.id.menuItemEditar){
                editarDadosPessoa();
                mode.finish();
                return true;
            }

            if(idMenuItem == R.id.menuItemExcluir){
                excluirDadosPessoa();
                mode.finish();
                return true;
            }

            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            if(viewSelecionada != null){
                viewSelecionada.setBackgroundColor(Color.TRANSPARENT);
            }
            actionMode = null;
            viewSelecionada = null;

            listViewDados.setEnabled(true);
        }
    };


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
                posicaoSelecionada = position;
                editarDadosPessoa();
               // Pessoa pessoa = (Pessoa) listViewDados.getItemAtPosition(position);
            }
        });

        listViewDados.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listViewDados.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){

            @Override
            public boolean onItemLongClick(AdapterView<?> parent,
                                           View view,
                                           int position,
                                           long id) {
                if(actionMode != null){
                    return false;
                }
                posicaoSelecionada = position;

                view.setBackgroundColor(Color.LTGRAY);
                viewSelecionada = view;
                listViewDados.setEnabled(false);
                actionMode = startSupportActionMode(mActionModeCallback);
                return false;
            }
        });

        popularLista();
        // popularListaDados();
    }


    private void popularLista(){
        PessoasDatabase database = PessoasDatabase.getDatabase(this);
        List<Pessoa> lista = database.pessoaDao().queryAll();

        listaAdapter = new PessoaAdapter(this,
                                        lista);
        listViewDados.setAdapter(listaAdapter);
    }

    private void excluirDadosPessoa(){
        listaDadosPessoa.remove(posicaoSelecionada);
        listaAdapter.notifyDataSetChanged();
    }
    ActivityResultLauncher<Intent> launcherEditarDadosPessoa =
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

                                    Pessoa pessoa = listaDadosPessoa.get(posicaoSelecionada);
                                    pessoa.setDtAvaliacao(dataAvaliacao);
                                    pessoa.setAltura(altura);
                                    pessoa.setDtNascimento(dataNascimento);
                                    pessoa.setPeso(peso);
                                    pessoa.setGenero(genero);
                                    pessoa.setObjetivo(objetivo);

                                    posicaoSelecionada = -1;

                                    listaAdapter.notifyDataSetChanged();
                                }
                            }
                        }
                    });

    private void editarDadosPessoa(){
        Pessoa pessoa = listaDadosPessoa.get(posicaoSelecionada);

        CadastroActivity.editarDadosPessoa(this,launcherEditarDadosPessoa,pessoa);
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

    //novaPessoa metodo
    public void novoDadoPessoa(View view){
        ListagemActivity.novoDadosPessoa(this,launcherNovoDadosPessoa);
    }

    public void popularListaDados(){
        PessoasDatabase database = PessoasDatabase.getDatabase(this);

        List<Pessoa> lista = database.pessoaDao().queryAll();

        listaAdapter = new PessoaAdapter(this,
                lista);

        listViewDados.setAdapter(listaAdapter);
    }

}