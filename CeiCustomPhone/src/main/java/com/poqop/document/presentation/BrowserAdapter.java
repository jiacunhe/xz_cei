package com.poqop.document.presentation;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileFilter;
import java.util.*;

import com.hyrt.ceiphone.R;


public class BrowserAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<HashMap<String, String>> name;
    
    public BrowserAdapter(Context context,ArrayList<HashMap<String, String>> arrayList){
    	this.context = context;
    	this.name = arrayList;
    }
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		Log.v("menu", "name.size()====="+name.size());
		return name.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	
	public Uri getUri(int position){
		return Uri.parse(name.get(position).get("filePath"));
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
	    View browserItem = LayoutInflater.from(context).inflate(R.layout.read_report_browseritem, null, false);
        ImageView imageView = (ImageView) browserItem.findViewById(R.id.browserItemIcon);
        imageView.setBackgroundResource(R.drawable.icon);
        TextView textView = (TextView) browserItem.findViewById(R.id.browserItemText);
        textView.setText(name.get(position).get("Name"));
		return browserItem;
	}
	
	
	
	
	
	
	
//    private final Context context;
//    private File currentDirectory;
//    private List<File> files = Collections.emptyList();
//    private final FileFilter filter;
//
//    public BrowserAdapter(Context context, FileFilter filter) {
//        this.context = context;
//        this.filter = filter;
//    }
//    
//    public int getCount() {
//        return files.size();
//    }
//
//    public File getItem(int i) {
//        return files.get(i);
//    }
//
//    public long getItemId(int i) {
//        return i;
//    }
//
//    public View getView(int i, View view, ViewGroup viewGroup) {
//        final View browserItem = LayoutInflater.from(context).inflate(R.layout.browseritem, viewGroup, false);
//        final ImageView imageView = (ImageView) browserItem.findViewById(R.id.browserItemIcon);
//        final File file = files.get(i);
//        final TextView textView = (TextView) browserItem.findViewById(R.id.browserItemText);
//        textView.setText(file.getName());
//        if (file.equals(currentDirectory.getParentFile()))
//        {
//            imageView.setImageResource(R.drawable.arrowup);
//            textView.setText(file.getAbsolutePath());
//        }
//        else if (file.isDirectory())
//        {
//            imageView.setImageResource(R.drawable.folderopen);
//        }
//        else
//        {
//            imageView.setImageResource(R.drawable.book);
//        }
//        return browserItem;
//    }
//
//    public void setCurrentDirectory(File currentDirectory)
//    {
//        final File[] fileArray = currentDirectory.listFiles(filter);
//        ArrayList<File> files = new ArrayList<File>(fileArray != null ? Arrays.asList(fileArray) : Collections.<File>emptyList());
//        this.currentDirectory = currentDirectory;
//        Collections.sort(files, new Comparator<File>()
//        {
//            public int compare(File o1, File o2)
//            {
//                if (o1.isDirectory() && o2.isFile()) return -1;
//                if (o1.isFile() && o2.isDirectory()) return 1;
//                return o1.getName().compareTo(o2.getName());
//            }
//        });
//        if (currentDirectory.getParentFile() != null)
//        {
//            files.add(0, currentDirectory.getParentFile());
//        }
//        setFiles(files);
//    }
//
//    public void setFiles(List<File> files)
//    {
//        this.files = files;
//        notifyDataSetInvalidated();
//    }
//
//    public File getCurrentDirectory()
//    {
//        return currentDirectory;
//    }
//	
	
}
