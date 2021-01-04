#!/usr/bin/env python3
#encoding=utf-8

import sys

inst_tpl = '''
class %sInst implements Instruction {
  @Override
  public void eval(Frame frame) {
    frame.pc += offset();
    throw new IllegalStateException();
  }

  @Override
  public int offset() {
    return 1;
  }

  static %sInst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    throw new IllegalStateException("parse %sInst");
  }
}'''

for line in sys.stdin:
    t = str(line).strip()
    tc = t.capitalize();
    x = inst_tpl % (tc, tc, tc) 
    print(x)
