package br.com.controlepeso;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import br.com.controlepeso.modelo.Pessoa;
import br.com.controlepeso.persistencia.PessoasDatabase;

public class CadastroActivity extends AppCompatActivity {

    public static final String MODO = "MODO";
    public static final String ID = "ID";
    public static final int NOVO = 1;
    public static final int ALTERAR = 2;

    private int modo;
    private Pessoa pessoa;

    private EditText editTextDateDtAvaliacao;
    private EditText editTextNumberAltura;
    private EditText editTextNumberPeso;
    private RadioGroup radioGroupGenero;
    private EditText editTextDateDtNascimento;
    private Spinner spinnerObjetivo;

    private EditText editTextImc;

    public static final String DT_AVALIACAO = "DT_AVALIACAO";
    public static final String ALTURA = "ALTURA";
    public static final String PESO = "PESO";
    public static final String GENERO = "GENERO";
    public static final String DT_NASCIMENTO = "DT_NASCIMENTO";
    public static final String OBJETIVO = "OBJETIVO";

    public static final String IMC = "IMC";

    public static final String IDA ="IDA";


    public static void nova(AppCompatActivity activity,int requestCode){

        Intent intent = new Intent(activity, CadastroActivity.class);

        intent.putExtra(MODO,NOVO);

        activity.startActivityForResult(intent,requestCode);
    }
    public void alterar(Activity activity, int requestCode, Pessoa pessoa){
        Intent intent = new Intent(activity, CadastroActivity.class);

        intent.putExtra(MODO,ALTERAR);
        intent.putExtra(ID,pessoa.getId());

        activity.startActivityForResult(intent,requestCode);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cadastro);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.listViewDados), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        editTextDateDtAvaliacao  = findViewById(R.id.editTextDateDtAvaliacao);
        editTextNumberAltura  = findViewById(R.id.editTextNumberAltura);
        editTextNumberPeso  = findViewById(R.id.editTextNumberPeso);
        radioGroupGenero  = findViewById(R.id.radioGroupGenero);
        editTextDateDtNascimento = findViewById(R.id.editTextDateDtNascimento);
        spinnerObjetivo = findViewById(R.id.spinnerObjetivo);
        popularSpinner();

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        modo = bundle.getInt(MODO, NOVO);

        if (modo == ALTERAR){

            setTitle(R.string.editar);

            int id = bundle.getInt(ID);

            PessoasDatabase database = PessoasDatabase.getDatabase(this);

            pessoa = database.pessoaDao().queryById(id);

            //setar os outros campos
            editTextDateDtAvaliacao.setText(pessoa.getDtAvaliacao());
            editTextNumberAltura.setText(pessoa.getAltura());
            editTextNumberPeso.setText(pessoa.getPeso());
            editTextDateDtNascimento.setText(pessoa.getDtNascimento());
            editTextImc.setText(pessoa.getImc());

            if(pessoa.getObjetivo().toString().equals(R.string.emagrecimento)){
                spinnerObjetivo.setSelected(true);
            }else{
                spinnerObjetivo.setSelected(false);
            }

            String sexo = pessoa.getGenero().toString();
            if(sexo.equals("Masculino")){
                radioGroupGenero.check(R.id.radioButtonMasculino);
            }else{
                radioGroupGenero.check(R.id.radioButtonFeminino);
            }

        }else{

            setTitle(R.string.novo);

            pessoa = new Pessoa();

            Date dataAtual = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            String date = dateFormat.format(dataAtual);
            editTextDateDtAvaliacao.setText(date);
        }
    }

    private void popularSpinner(){
        ArrayList<String> lista = new ArrayList<>();

        lista.add("Emagrecimento");
        lista.add("Ganho massa muscular");

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(this,
                        android.R.layout.simple_list_item_1,
                        lista);
        spinnerObjetivo.setAdapter(adapter);
    }

    public void sobre(View view){
        SobreActivity.nova(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.cadastro_opcoes,menu);
        return true;
    }

    //metodo para chamar os botoes da barra de cima
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int menuSelecionado = item.getItemId();

        if(menuSelecionado == R.id.menuItemSobre){
            sobre(item.getActionView());
            return true;
        }

        if(menuSelecionado == R.id.menuItemSalvar){
            salvar();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void salvar(){

        String dataAvaliacao = editTextDateDtAvaliacao.getText().toString();
        String altura = editTextNumberAltura.getText().toString();
        String peso = editTextNumberPeso.getText().toString();
        //  String genero = editTe
        String dataNascimento = editTextDateDtNascimento.getText().toString();
        String objetivo =  (String) spinnerObjetivo.getSelectedItem();

        if(dataAvaliacao == null || dataAvaliacao.trim().isEmpty()){
            Toast.makeText(this, getString(R.string.dataAvalicao) +
                            " " + getString(R.string.n_o_pode_ser_vazio),
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if(altura == null || altura.trim().isEmpty()){
            Toast.makeText(this, getString(R.string.altura) +
                            " " + getString(R.string.n_o_pode_ser_vazio),
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if(peso == null || peso.trim().isEmpty()){
            Toast.makeText(this, getString(R.string.peso) +
                            " " + getString(R.string.n_o_pode_ser_vazio),
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if(dataNascimento == null || dataNascimento.trim().isEmpty()){
            Toast.makeText(this, getString(R.string.dtNascimento) +
                            " " + getString(R.string.n_o_pode_ser_vazio),
                    Toast.LENGTH_SHORT).show();
            return;
        }

        // pessoa.setId();
        pessoa.setDtAvaliacao(dataAvaliacao);
        pessoa.setPeso(peso);
        pessoa.setObjetivo(objetivo);
        pessoa.setIda(CalculaUtils.calculaIDA(peso));
        pessoa.setImc(CalculaUtils.calculaIMC(altura,peso));
        pessoa.setIda(CalculaUtils.calculaIDA(peso));

        PessoasDatabase database = PessoasDatabase.getDatabase(this);

        if(modo == NOVO){
            database.pessoaDao().insert(pessoa);
        }else{
            database.pessoaDao().update(pessoa);
        }
        setResult(Activity.RESULT_OK);
        finish();
    }
}