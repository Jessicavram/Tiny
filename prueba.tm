begin
int i,v[10],x,y[10];

read x;

for(i:=0;i<x and i<10;i:=i+1)
	v[i] := i*i;
	write v[i];
	y[i] := v[(i+1)/2]+10;
	write y[i];
end;

write 0;
write 0;
write 0;

for(i:=x;i<10;i:=i+1)
	read v[i];
	y[i] := v[i]+5;
	write y[i]; 
end;
end