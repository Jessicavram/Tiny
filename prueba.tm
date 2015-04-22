begin 
	
int r;
read r;
write -4--4-4*-1>2;
return;
	int v[5],b[5],i,j;
	int aux;
	v[0]:=5;
	v[1]:=7;
	v[2]:=2;
	v[3]:=9;
	read v[4];
	i:=0;
	aux:=0;
	for(i:=0;i<5;i:=i+1)
		for(j:=0;j<4;j:=j+1)
			if v[j+1]<v[j] then
				aux:=v[j+1];
				v[j+1]:=v[j];
				v[j]:=aux;
			end;	
		end;
	end;
	for(i:=0;i<5;i:=i+1)
		write v[i];
	end;	

        int x,y;
repeat 
read x;

        if x>20 or x=2 and x < 10  then
           write 999;
        else
           write  0;
	end;
	read y;
until y=0;
end