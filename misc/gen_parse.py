#!/usr/bin/env python3
#encoding=utf-8

import sys

header = '''
  private void parser(List<Instruction> instructions, int opcode, DataInputStream dis, CpInfo[] cp) {
    switch (opcode) {'''
print(header)

for line in sys.stdin:
    t = str(line).strip()
    tu = t.upper();
    x = '      case Const.OPC_{}:\n        instructions.add({}Inst.parse(dis, cp)); break;'.format(tu, t.capitalize())
    print(x)

footer='''
      default:
        throw new IllegalStateException("unknown opcode " + opcode);
    }
  }'''
print(footer)