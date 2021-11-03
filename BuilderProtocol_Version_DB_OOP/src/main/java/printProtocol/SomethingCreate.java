package printProtocol;

import com.lowagie.text.Cell;
import com.lowagie.text.Element;
import com.lowagie.text.Rectangle;

import java.awt.*;

public class SomethingCreate {



    public static Cell createCellNoBorder(String CellString){ //создание 1 ячейки, на вход строка
        Cell cell = new Cell(CellString);
        cell.setBorder(Rectangle.NO_BORDER); // устанавливаем прозрачные границ
        return cell;

    }

    public static Cell createCellForNameFields(String CellString){ //создание 1 ячейки, на вход строка

        Cell cell = new Cell(CellString);
        cell.setBackgroundColor(Color.lightGray); // закраска заднего фона
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);//текст в ячейке прижимается к левому краю


        return cell;

    }

    public static Cell createCellForNumbersInTables(String CellString){ //создание 1 ячейки, на вход строка

        Cell cell = new Cell(CellString);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);//текст в ячейке прижимается к левому краю

        return cell;

    }

    public static Cell createCellForValues(String CellString){ //создание 1 ячейки, на вход строка

        Cell cell = new Cell(CellString);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);//текст в ячейке прижимается к правому краю


        return cell;

    }



}
