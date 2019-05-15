package com.occ.service;

import java.text.ParseException;
import java.util.List;

public interface ExcelService {

    void saveBath(List<String[]> list) throws ParseException;
}
