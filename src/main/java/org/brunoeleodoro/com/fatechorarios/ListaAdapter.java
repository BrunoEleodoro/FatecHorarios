package org.brunoeleodoro.com.fatechorarios;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


/**
 * Created by bruno on 16/02/17.
 */

public class ListaAdapter extends BaseAdapter {
    Activity context;
    String[] aulas;
    String[] professores;
    String[] horarios;
    public ListaAdapter(Activity context,String[] aulas,String[] professores,String[] horarios)
    {
        this.context = context;
        this.aulas = aulas;
        this.professores = professores;
        this.horarios = horarios;
    }
    /*
    ArrayList<String> aulas = new ArrayList<>();
    ArrayList<String> professores = new ArrayList<>();
    ArrayList<String> horarios = new ArrayList<>();

    public ListaAdapter(Context context,ArrayList<String> aulas,ArrayList<String> professores,ArrayList<String> horarios)
    {

    }
    */
    @Override
    public int getCount() {
        return aulas.length;
    }

    @Override
    public Object getItem(int i) {
        return aulas[i]+";"+professores[i]+";"+horarios[i];
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }
    static class ViewHolder {
        TextView aula;
        TextView professor;
        TextView horario;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        LayoutInflater inflater =  context.getLayoutInflater();
        if (view == null)
        {
            view = inflater.inflate(R.layout.linha, null);
            holder = new ViewHolder();
            holder.aula = (TextView) view.findViewById(R.id.txt_aula);
            holder.professor = (TextView) view.findViewById(R.id.txt_professor);
            holder.horario = (TextView) view.findViewById(R.id.txt_hora);
            view.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) view.getTag();
        }

        holder.aula.setText(aulas[i]);
        holder.professor.setText(professores[i]);
        holder.horario.setText(horarios[i]);

        return view;
    }
}
