package up.edu.br.contatos;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;



public class ContatoAdapter extends BaseAdapter {

    private List<Contato> contatos;
    Activity act;

    public ContatoAdapter(List<Contato> contatos,
                          Activity act) {
        this.contatos = contatos;
        this.act = act;
    }


    @Override
    public int getCount() {
        return this.contatos.size();
    }

    @Override
    public Object getItem(int i) {
        return this.contatos.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view,
                        ViewGroup viewGroup) {

        View v = act.getLayoutInflater()
                .inflate(R.layout.contato_adapter,
                        viewGroup, false);

        TextView textView1 = v.findViewById(R.id.textView);
        TextView textView2 = v.findViewById(R.id.textView2);
        ImageView imageView = v.findViewById(R.id.imageView4);

        Contato c = contatos.get(i);

        textView1.setText(c.getNome());
        textView2.setText(c.getTelefone());

        if (c.getTipo().equals("Celular")) {
            imageView.
                    setImageResource(
                            android.R.drawable.ic_menu_call);
        } else {
            imageView.
                    setImageResource(
                            android.R.drawable.ic_dialog_dialer);
        }

        return v;
    }

    public void remove(Contato contato) {
        this.contatos.remove(contato);
    }
}
