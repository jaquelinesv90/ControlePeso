package br.com.controlepeso;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import br.com.controlepeso.modelo.Pessoa;

public class PessoaAdapter extends BaseAdapter {

    private Context context;
    private List<Pessoa> pessoas;

    //
    private static class PessoaHolder{
        public TextView textViewValorId;
        public TextView textViewValorDtAvaliacao;
        public TextView textViewValorAltura;
        public TextView textViewValorPeso;
        public TextView textViewValorGenero;
        public TextView textViewValorDtNascimento;
        public TextView textViewValorObjetivo;

        public TextView textViewValorImc;

        public TextView textViewValorIda;

    }
    public PessoaAdapter(Context context, List<Pessoa> pessoas){
        this.context = context;
        this.pessoas = pessoas;
    }


    @Override
    public int getCount() {
        return pessoas.size();
    }

    @Override
    public Object getItem(int position) {
        return pessoas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PessoaHolder holder;

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView  = inflater.inflate(R.layout.linha_lista_dados_pessoa,parent,false);

            holder = new PessoaHolder();

            holder.textViewValorId = convertView.findViewById(R.id.textViewValorId);
            holder.textViewValorDtAvaliacao = convertView.findViewById(R.id.textViewValorDtAvaliacao);

            // holder.textViewValorAltura = convertView.findViewById(R.id.textViewValorAltura);
            //  holder.textViewValorGenero = convertView.findViewById(R.id.textViewValorGenero);
            // holder.textViewValorDtNascimento = convertView.findViewById(R.id.textViewDtNasc);

            holder.textViewValorPeso = convertView.findViewById(R.id.textViewValorPeso);
            holder.textViewValorObjetivo = convertView.findViewById(R.id.textViewValorObjetivo);
            holder.textViewValorImc = convertView.findViewById(R.id.textViewValorImc);
            holder.textViewValorIda = convertView.findViewById(R.id.textViewValorIda);

            convertView.setTag(holder);

        }else{
            holder = (PessoaHolder) convertView.getTag();
        }

        // holder.textViewValorId.setText(pessoas.get(position).getId());
        holder.textViewValorDtAvaliacao.setText(pessoas.get(position).getDtAvaliacao());
        //holder.textViewValorAltura.setText(pessoas.get(position).getAltura());
        holder.textViewValorPeso.setText(pessoas.get(position).getPeso());
        holder.textViewValorImc.setText(pessoas.get(position).getImc());
        holder.textViewValorIda.setText(pessoas.get(position).getIda());


        //Genero genero = pessoas.get(position).getGenero();
        //if(genero.equals(Genero.MASCULINO)){
        // holder.textViewValorGenero.setText("Masculino");
        // }else{
        //   holder.textViewValorGenero.setText("Feminino");
        // }

        // holder.textViewValorDtNascimento.setText(pessoas.get(position).getDtNascimento());
        holder.textViewValorObjetivo.setText(pessoas.get(position).getObjetivo());

        return convertView;
    }
}
