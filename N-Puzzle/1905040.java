import java.util.*;

import static java.lang.Math.abs;
import static java.lang.System.exit;


public class Main {
    static int size;
    static int inversion_count(int[] a){
        int count=0;
        for(int i=0;i<a.length;i++){
            for(int j=i+1;j<a.length;j++){
                if(a[i]>a[j])
                    count++;
            }
        }
        return count;
    }
    static boolean solvability_check(int inv,int blank){
        if(size%2!=0 && inv%2!=0){
            System.out.println("Unsolvable");
            return false;
        }
        else if(size%2==0 && (inv+blank)%2!=0){
            System.out.println("Unsolvable");
            return false;
        }
        return true;
    }
    static int hamming_value(int[] a){
        int value=0;
        for(int i=0;i<a.length;i++){
            if(a[i]!=(i+1) && a[i]!=0){
                value++;
            }
        }
        return value;
    }
    static int manhattan_value(int[][] a){
        int value=0;
        for(int i=0;i<size;i++){
            for(int j=0;j<size;j++){
                if(a[i][j]==0){

                }
                else{
                    value+=Math.abs(i-(a[i][j]-1)/size)+Math.abs(j-(a[i][j]-1)%size);
                }
            }
        }
        return value;
    }
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        size=sc.nextInt();
        Node initial=new Node(size);
        Node result;
        int[] p=new int[size*size-1];
        int[] t=new int[size*size];
        int[][] input_mat=new int[size][size];
        int temp=0,tmp=0,blank_i=0,blank_j=0;
        for(int i=0;i<size;i++){
            for (int j=0;j<size;j++){
                input_mat[i][j]= sc.nextInt();
                if(input_mat[i][j]!=0) {
                    p[temp] = input_mat[i][j];
                    t[tmp]=input_mat[i][j];
                    tmp++;
                    temp++;

                }
                else{
                    t[tmp]=input_mat[i][j];
                    tmp++;
                   blank_i=i;
                   blank_j=j;
                }
            }
            sc.nextLine();
        }
        initial.search_node_uno=input_mat;
        initial.blank_row=blank_i;
        initial.blank_col=blank_j;
        if(!solvability_check(inversion_count(p),size-blank_i-1)) {
            exit(0);
        }
        PriorityQueue<Node> nodelist=new PriorityQueue<>();
        nodelist.add(initial);
        while(true){
            Node temporary;
            int[][] t_matrix=new int[size][size];
            temporary=nodelist.poll();
            for(int i=0;i<size;i++){
                for(int j=0;j<size;j++){
                    t_matrix[i][j]=temporary.search_node_uno[i][j];
                }
            }
//            System.out.println("This is from temporary");
//            print(temporary.search_node_uno);
            if(temporary.blank_row!=size-1){

                t_matrix[temporary.blank_row][temporary.blank_col]=temporary.search_node_uno[temporary.blank_row+1][temporary.blank_col];
                t_matrix[temporary.blank_row+1][temporary.blank_col]=0;
//                System.out.println("Now we have been changed to t-mat which is:");
//                print(t_matrix);
                Node w=new Node(size);
                w.blank_row=temporary.blank_row+1;
                w.blank_col=temporary.blank_col;
                w.search_node_uno=t_matrix;
                w.parent=temporary;
                w.cost= w.parent.cost+1;
                //w.h_cost=manhattan_value(w.search_node_uno);
                w.copy_from_matrix_to_array(w.search_node_uno);
                w.h_cost=hamming_value(w.hamming_helper);
                if(w.h_cost==0){
                    result=w;
                    break;
                }
                else{
                    nodelist.add(w);
                }
            }
            if(temporary.blank_row!=0){
                t_matrix=new int[size][size];
                for(int i=0;i<size;i++){
                    for(int j=0;j<size;j++){
                        t_matrix[i][j]=temporary.search_node_uno[i][j];
                    }
                }
                t_matrix[temporary.blank_row][temporary.blank_col]=temporary.search_node_uno[temporary.blank_row-1][temporary.blank_col];
                t_matrix[temporary.blank_row-1][temporary.blank_col]=0;
//                System.out.println("Now we have been changed to t-mat which is:");
//                print(t_matrix);
                Node w=new Node(size);
                w.blank_row=temporary.blank_row-1;
                w.blank_col=temporary.blank_col;
                w.search_node_uno=t_matrix;
                w.parent=temporary;
                w.cost= w.parent.cost+1;
                //w.h_cost=manhattan_value(w.search_node_uno);
                w.copy_from_matrix_to_array(w.search_node_uno);
                w.h_cost=hamming_value(w.hamming_helper);
                if(w.h_cost==0){
                    result=w;
                    break;
                }
                else{
                    nodelist.add(w);
                }
            }
            if(temporary.blank_col!=size-1){
                t_matrix=new int[size][size];
                for(int i=0;i<size;i++){
                    for(int j=0;j<size;j++){
                        t_matrix[i][j]=temporary.search_node_uno[i][j];
                    }
                }
                t_matrix[temporary.blank_row][temporary.blank_col]=temporary.search_node_uno[temporary.blank_row][temporary.blank_col+1];
                t_matrix[temporary.blank_row][temporary.blank_col+1]=0;
//                System.out.println("Now we have been changed to t-mat which is:");
//                print(t_matrix);
                Node w=new Node(size);
                w.blank_row=temporary.blank_row;
                w.blank_col=temporary.blank_col+1;
                w.search_node_uno=t_matrix;
                w.parent=temporary;
                w.cost= w.parent.cost+1;
                //w.h_cost=manhattan_value(w.search_node_uno);
                w.copy_from_matrix_to_array(w.search_node_uno);
                w.h_cost=hamming_value(w.hamming_helper);
                if(w.h_cost==0){
                    result=w;
                    break;
                }
                else{
                    nodelist.add(w);
                }
            }
            if(temporary.blank_col!=0){
                t_matrix=new int[size][size];
                for(int i=0;i<size;i++){
                    for(int j=0;j<size;j++){
                        t_matrix[i][j]=temporary.search_node_uno[i][j];
                    }
                }
                t_matrix[temporary.blank_row][temporary.blank_col]=temporary.search_node_uno[temporary.blank_row][temporary.blank_col-1];
                t_matrix[temporary.blank_row][temporary.blank_col-1]=0;
//                System.out.println("Now we have been changed to t-mat which is:");
//                print(t_matrix);
                Node w=new Node(size);
                w.blank_row=temporary.blank_row;
                w.blank_col=temporary.blank_col-1;
                w.search_node_uno=t_matrix;
                w.parent=temporary;
                w.cost= w.parent.cost+1;
                //w.h_cost=manhattan_value(w.search_node_uno);
                w.copy_from_matrix_to_array(w.search_node_uno);
                w.h_cost=hamming_value(w.hamming_helper);
                if(w.h_cost==0){
                    result=w;
                    break;
                }
                else{
                    nodelist.add(w);
                }
            }
        }
        Stack<Node> k=new Stack<>();
        while(result!=null){
            k.add(result);
            result=result.parent;
        }
        System.out.println("Minimum number of moves = "+(k.size()-1));
        while(k.size()!=0){
            print(k.pop().search_node_uno);
        }
    }
    static void print(int[][] p){
        //System.out.println("parent of ager jon coming");
        for(int i=0;i<size;i++){
            for(int j=0;j<size;j++){
                System.out.print(p[i][j]+" ");
            }
            System.out.println();
        }
        System.out.println();
    }

}
class Node implements Comparable<Node>{
    int[][] search_node_uno;
    int[] hamming_helper;
    int cost=0,dimension;
    Node parent;
    int blank_row,blank_col;
    int h_cost=0;
    Node(int size){
        dimension=size;
        parent=null;
        search_node_uno=new int[size][size];
        hamming_helper=new int[size*size];
    }
    void copy_from_matrix_to_array(int[][] r){
        int temp=0;
        for(int i=0;i<dimension;i++){
            for(int j=0;j<dimension;j++){
                //System.out.println(i+"  "+j+"  "+dimension);
                hamming_helper[temp]=r[i][j];
                temp++;
            }
        }
    }

    @Override
    public int compareTo(Node o) {
        return (this.cost+this.h_cost)-(o.cost+o.h_cost);
    }
}