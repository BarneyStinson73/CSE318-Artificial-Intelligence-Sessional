import java.util.Arrays;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        int input;
        Scanner sc=new Scanner(System.in);
        state start=new state();
        game ongoing=new game(start);
        while(!ongoing.over_check()){
            int best_score = 0;
            if (ongoing.present.next == 1) {
                best_score = Integer.MIN_VALUE;
            }
            else best_score = Integer.MAX_VALUE;
            int best_move =-1;
            for(int i=0;i<6;i++){
                if(ongoing.present.next==1){
                    if(ongoing.present.player1[i]==0)
                        continue;
                }
                if(ongoing.present.next==2){
                    if(ongoing.present.player2[i]==0)
                        continue;
                }
                state temp=ongoing.move(i,ongoing.present.next,ongoing.present);
                temp.depth=1;
                int score;
                //int score = ongoing.min_max(temp.next,temp, 1);
                if(temp.next==1) {
                    score = ongoing.alpha_beta(temp.next, temp, 2, Integer.MIN_VALUE, Integer.MAX_VALUE, ongoing.present.next);
                    // score = ongoing.min_max(temp.next,temp, 1);
                }
                else {
                    score = ongoing.alpha_beta(temp.next, temp, 1, Integer.MIN_VALUE, Integer.MAX_VALUE, ongoing.present.next);
                }
                if (ongoing.present.next==1) {
                    if (best_score <= score) {
                        best_score = score;
                        best_move = i;
                    }
                }
                else {
                    if (best_score >= score) {
                        best_score = score;
                        best_move = i;
                    }
                }
            }
            System.out.println("now moves:"+ongoing.present.next+"best move:"+best_move+"best score:"+best_score);
            if(ongoing.present.next==1&& ongoing.present.player1[best_move]==0) System.out.println("WhY?");
            ongoing.present=ongoing.move(best_move,ongoing.present.next,ongoing.present);
            ongoing.printBoard();
        }
    }
}
class state{
    int[] player1;
    int[] player2;
    int bin1;
    int bin2;
    int next;
    int depth;
    int additional_move;
    int capture;
    state(){
        player1=new int[6];
        player2=new int[6];
        bin1=0;
        bin2=0;
        next=1; // next move will be taken by player 1,state will determine using 1 or 2
        for(int i=0;i<6;i++){
            player1[i]=4;
            player2[i]=4;
        }
        depth=0;
        additional_move=0;
        capture=0;
    }

}
class game{
    state present;
    game(state s){
        present=s;

        int a=0,b=0,turn=1,check=0;
        }
    boolean over_check(){
        int test=0;
        for(int i=0;i<6;i++){
            if(present.player1[i]!=0){
                test=1;
                break;
            }
        }
        if(test==0)
            return true;
        test=0;
        for(int i=0;i<6;i++){
            if(present.player2[i]!=0){
                test=1;
                break;
            }
        }
        if(test==0)
            return true;
        return false;
    }
    state move(int index,int player,state received_state){
        int turn=1;
        int temp;
        state s=new state();
        s.player1= Arrays.copyOf(received_state.player1,6);
        s.player2= Arrays.copyOf(received_state.player2,6);
        s.bin1=received_state.bin1;
        s.bin2=received_state.bin2;
        s.next=received_state.next;
        s.depth=received_state.depth+1;
        s.capture=received_state.capture;
        s.additional_move= received_state.additional_move;
        if(player==1){
            temp=s.player1[index];
            s.player1[index]=0;
            //System.out.println(temp);
            if(temp<=(6-index-1)){

                for(int i=1;i<=temp;i++){
                    s.player1[index+1]++;
                    index++;
                }
                if(s.player1[index]==1 && s.player2[5-index]!=0){   //checking
                    s.bin1+=s.player2[5-index]+1;
                    s.player2[5-index]=0;
                    s.player1[index]=0;
                    s.capture++;
                }
                if(s.next==1)
                    s.next=2;
                else if(s.next==2)
                    s.next=1;

            }
            else if(temp==(6-index)){
                for(int i=index+1;i<6;i++){
                    s.player1[i]++;
                }
                s.bin1++;
                s.additional_move++;

            }
            else if(temp<=(12-index)){
                for(int i=index+1;i<6;i++){
                    s.player1[i]++;
                    temp--;
                }
                s.bin1++;
                temp--;
                for(int i=0;i<temp;i++){
                    s.player2[i]++;
                }
                if(s.next==1)
                    s.next=2;
                else if(s.next==2)
                    s.next=1;
            }
            else if(temp>(12-index)){
                boolean side=true,storage_in=false;
                while(temp!=0){
                    if(index==6 && side==true){
                        s.bin1++;
                        temp--;
                        side=false;
                        index=0;
                    }
                    else if(index==6 && side==false){
                        side=true;
                        index=0;
                    }
                    else if(side==true){
                        s.player1[index]++;
                        temp--;
                        if(temp==0 && s.player1[index]==1 && s.player2[5-index]!=0){
                            s.bin1+=s.player2[5-index]+1;
                            s.player1[index]=0;
                            s.player2[5-index]=0;
                            s.capture++;
                        }
                        index++;
                    }
                    else if(side==false){
                        s.player2[index]++;
                        temp--;
                        index++;
                    }
                }

                if(s.next==1)
                    s.next=2;
                else if(s.next==2)
                    s.next=1;
            }

        }
        if(player==2){
            temp=s.player2[index];
            s.player2[index]=0;
            if(temp<=(6-index-1)){

                for(int i=1;i<=temp;i++){
                    s.player2[index+1]++;
                    index++;
                }
                if(s.player2[index]==1 && s.player1[5-index]!=0){
                    s.bin2+=s.player1[5-index]+1;
                    s.player2[index]=0;
                    s.player1[5-index]=0;
                    s.capture--;
                }
                if(s.next==1)
                    s.next=2;
                else if(s.next==2)
                    s.next=1;
            }
            else if(temp==(6-index)){
                for(int i=index+1;i<6;i++){
                    s.player2[i]++;
                }
                s.bin2++;
                s.additional_move--;
            }
            else if(temp<=(12-index)){
                for(int i=index+1;i<6;i++){
                    s.player2[i]++;
                    temp--;
                }
                s.bin2++;
                temp--;
                for(int i=0;i<temp;i++){
                    s.player1[i]++;
                }
                if(s.next==1)
                    s.next=2;
                else if(s.next==2)
                    s.next=1;
            }
            else if(temp>(12-index)){

                boolean side=true,storage_in=false;
                while(temp!=0){
                    if(index==6 && side==true){
                        s.bin2++;
                        temp--;
                        side=false;
                        index=0;
                    }
                    else if(index==6 && side==false){
                        side=true;
                        index=0;
                    }
                    else if(side==true){
                        s.player2[index]++;
                        temp--;
                        if(temp==0 && s.player2[index]==1 && s.player1[5-index]!=0){
                            s.bin2+=s.player1[5-index]+1;
                            s.player2[index]=0;
                            s.player1[5-index]=0;
                            s.capture--;
                        }
                        index++;
                    }
                    else if(side==false){
                        s.player1[index]++;
                        temp--;
                        index++;
                    }
                }

                //s.next=(s.next+1)%2;
                if(s.next==1)
                    s.next=2;
                else if(s.next==2)
                    s.next=1;

            }

        }
        return s;
    }
    int heuristics(int heu_no,int player,state s){
        int w1=1,w2=1,w3=1,w4=1,stones1=0,stones2=0;
        for(int i=0;i<6;i++){
            stones1+=s.player1[i];
            stones2+=s.player2[i];
        }
        if(heu_no==1){
            return s.bin1- s.bin2;
        }
        else if(heu_no==2){
            return w1*(s.bin1- s.bin2)+w2*(stones1-stones2);
        }
        else if(heu_no==3){
            return w1*(s.bin1- s.bin2)+w2*(stones1-stones2)+w3*(s.additional_move);
        }
        else if(heu_no==4){
            return w1*(s.bin1- s.bin2)+w2*(stones1-stones2)+w3*(s.additional_move)+w4*s.capture;
        }
        return  0;
    }
    int min_max(int player,state s, int heuristic_no){
        int stones1=0,stones2=0;
        for(int i=0;i<6;i++){
            stones1+=s.player1[i];
            stones2+=s.player2[i];
        }
        if(stones2==0 || stones1==0){
            if((stones1+s.bin1)>(stones2+s.bin2)){
                return Integer.MAX_VALUE;
            }
            else if((stones1+s.bin1)<(stones2+s.bin2)){
                return Integer.MIN_VALUE;
            }

        }
        else if (s.depth >= 10) {
            s.depth=0;
            return heuristics(heuristic_no, player, s);
        }
        int best_score = 0;
        if (player == 1) {
            best_score = Integer.MIN_VALUE;
        }
        else best_score = Integer.MAX_VALUE;
        int best_move = 0;
        for(int i=0;i<6;i++){
                if(player==1){
                    if(s.player1[i]==0)
                        continue;
                }
                if(player==2){
                    if(s.player2[i]==0)
                        continue;
                }
                int score = min_max(player,move(i,player,s), heuristic_no);
                if (player==1) {
                    if (best_score <= score) {
                        best_score = score;
                        best_move = i;
                    }
                }
                else {
                    if (best_score >= score) {
                        best_score = score;
                        best_move = i;
                    }
                }
        }
        return best_score;
    }

    int alpha_beta(int player,state s, int heuristic_no,int alpha,int beta,int parent_player){
        int stones1=0,stones2=0;
        for(int i=0;i<6;i++){
            stones1+=s.player1[i];
            stones2+=s.player2[i];
        }
        if(stones2==0 || stones1==0){
            if((stones1+s.bin1)>(stones2+s.bin2)){
                return Integer.MAX_VALUE;
            }
            else if((stones1+s.bin1)<(stones2+s.bin2)){
                return Integer.MIN_VALUE;
            }

        }
        else if (s.depth >= 10) {
            return heuristics(heuristic_no, player, s);
        }
        int best_score = 0;
        if (player == 1) {
            best_score = Integer.MIN_VALUE;
        }
        else best_score = Integer.MAX_VALUE;
        int best_move = 0;
        for(int i=0;i<6;i++){
            if(player==1){
                if(s.player1[i]==0)
                    continue;
            }
            if(player==2){
                if(s.player2[i]==0)
                    continue;
            }
            int score;
            if(player==1)
                score = alpha_beta(player,move(i,player,s), heuristic_no,best_score,beta, present.next);
            else
                score = alpha_beta(player,move(i,player,s), heuristic_no,alpha,best_score, present.next);

            if (player==1) {
                if (best_score <= score) {
                    best_score = score;
                    best_move = i;
                    if(parent_player!=player && best_score>beta ){
                        return best_score;
                    }
                }
            }
            else {
                if (best_score >= score) {
                    best_score = score;
                    best_move = i;
                    if(parent_player!=player && best_score<alpha ){
                        return best_score;
                    }
                }
            }
        }
        return best_score;
    }

    void printBoard(){
        System.out.println("Player 1");
        System.out.println("Index 1\tIndex 2\tIndex 3\tIndex 4\tIndex 5\tIndex 6\t");
        System.out.println("\t"+ present.player1[0]+"\t"+
                "\t"+ present.player1[1]+"\t"+
                "\t"+ present.player1[2]+"\t"+
                "\t"+ present.player1[3]+"\t"+
                "\t"+ present.player1[4]+"\t"+
                "\t"+ present.player1[5]+"\t");
        System.out.println("Storage of player 1:"+present.bin1);
        System.out.println("Player 2");
        System.out.println("Index 1\tIndex 2\tIndex 3\tIndex 4\tIndex 5\tIndex 6\t");
        System.out.println("\t"+ present.player2[0]+"\t"+
                "\t"+ present.player2[1]+"\t"+
                "\t"+ present.player2[2]+"\t"+
                "\t"+ present.player2[3]+"\t"+
                "\t"+ present.player2[4]+"\t"+
                "\t"+ present.player2[5]+"\t");
        System.out.println("Storage of player 2:"+present.bin2);
        }

}