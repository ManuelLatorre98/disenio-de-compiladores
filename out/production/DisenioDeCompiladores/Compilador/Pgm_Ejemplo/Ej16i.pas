program prueba ;{ Archivo Prueba C&I: EJ16I.PAS }
var
  a,b:integer;
function algo(x, y:integer): integer;
 begin
    x:= 10;
    algo:= x + y + 10;  
 end;
begin
  a:=9;
  algo( a );
end.

