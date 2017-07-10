/*
 * Copyright (C) WIZnet, Inc. All rights reserved.
 * Use is subject to license terms.
 */
#include "WizFiRingBuffer.h"

WizFiRingBuffer::WizFiRingBuffer( int size )
{
    bufferSize = size+1;
    bufferTail = 0;
    bufferHead = 0;
    buffer_p = ( byte* )malloc( bufferSize );
        memset( buffer_p, 0, bufferSize );
}

WizFiRingBuffer::~WizFiRingBuffer()
{
    if( buffer_p )
          free( buffer_p );
}

// public functions
int WizFiRingBuffer::reset()
{
    bufferHead = 0;
    bufferTail = 0;

    memset(buffer_p, 0, bufferSize);
}

int WizFiRingBuffer::available(void)
{
  return (bufferSize + bufferHead - bufferTail) % bufferSize;
}

bool WizFiRingBuffer::isFull()
{
    if( available() >= bufferSize - 1) {
		return true;
	}
	else 
		return false;
}

int WizFiRingBuffer::getBufferSize()
{
    return bufferSize - 1;
}

int WizFiRingBuffer::read(void)
{
  if (bufferHead == bufferTail) {
    return -1;
  }
  else {
    byte c = buffer_p[bufferTail];
    bufferTail = (bufferTail + 1) % bufferSize;
    if(bufferHead == bufferTail) {
      bufferTail = 0;
      bufferHead = 0;
    }
	
    return c;
  }
}

unsigned int WizFiRingBuffer::read(uint8_t* buf, unsigned int size)
{
  if (bufferHead == bufferTail) {
    return 0;
  }
  else {
	int dataSize = available();
	int len = ((size < dataSize) ? size : dataSize);
	if ((bufferTail + len) / bufferSize) {
		int leftSize = bufferSize - bufferTail;
		memcpy(buf, buffer_p + bufferTail, leftSize);
		memcpy(buf + leftSize, buffer_p, len - leftSize);
	}
	else {
		memcpy(buf, buffer_p + bufferTail, len);
	}
    bufferTail = (bufferTail + len) % bufferSize;
    if(bufferHead == bufferTail) {
      bufferTail = 0;
      bufferHead = 0;
    }
    return len;
  }
}

bool WizFiRingBuffer::write( int c )
{
  if (isFull()) {
    return false;
  }
  
  buffer_p[bufferHead] = c;
  bufferHead = (bufferHead + 1) % bufferSize;
  return true;
}

bool WizFiRingBuffer::remove(int n)
{
  if(available() >= n) {
    bufferTail = (bufferTail + n) % bufferSize;
	return true;
  }
  else
	  return false;
}

int WizFiRingBuffer::peek(void)
{
  if (bufferHead == bufferTail) {
    return -1;
  }
  else {
    return buffer_p[bufferTail];
  }
}

int WizFiRingBuffer::peek(int n) {

  if (available() < n) {
    return -1;
  }
  else {
    return buffer_p[(bufferTail + n) % bufferSize];
  }
}

void WizFiRingBuffer::init()
{
    reset();
}

void WizFiRingBuffer::push(char c)
{
    write(c);
}

bool WizFiRingBuffer::endsWith(const char* str)
{
    int buffer_len;

    int findStrLen = strlen(str);

    char *p1 = (char*)&str[0];
    char *p2 = p1 + findStrLen;

    unsigned int tail = bufferHead - findStrLen;

    for(char *p=p1; p<p2; p++)
    {
        if(*p != buffer_p[tail])
        {
            return false;
        }

        tail++;

        if( tail == bufferSize)     tail = 0;
    }

    return true;
}

char* WizFiRingBuffer::FindStr(const char* str)
{
    char *p_find_start = (char*)&str[0];
    char *p_src_start  = (char*)&buffer_p[bufferTail];

    return strstr((char*)p_src_start,(char*)p_find_start);
}


int WizFiRingBuffer::getString(char* dest, int size)
{
    char *p = (char*)&buffer_p[bufferTail];
    strncpy((char*)dest,(char*)p,size);
    return 0;
}

void WizFiRingBuffer::getStrN(char *dest, unsigned int skipChars, unsigned int num)
{
    int len = ( bufferSize + bufferHead - bufferTail) % bufferSize - skipChars;

    if (len > num)
        len = num;

    char *p = (char*)&buffer_p[bufferTail];
    strncpy(dest,(char*)p,len);
    dest[len]=0;
}


int WizFiRingBuffer::getLine(char* dest, int size, char sep, int skipchar)
{
    int i,len=0,byteCnt=0;
    char ch;


    byteCnt = available();

    for(i=0; i<byteCnt; i++)
    {
        ch = (char)read();
        if(ch == sep)       break;

        if(i > size)
        {
            continue;
        }

        dest[i] = ch;
    }
    len = i;


    for(i=0;i<skipchar;i++) read();

    return len;
}

void WizFiRingBuffer::printbuffer()
{
	int i = 0;
	
	for (i = 0; true; i++) {
		Serial.print((char)buffer_p[bufferTail + i]);
		if (((bufferTail + i) % bufferSize) == bufferHead)
			break;
	}
	Serial.println();
	Serial.println(i);
}

char* WizFiRingBuffer::getStr()
{
    char *p = (char*)&buffer_p[bufferHead];
    *p = 0;

    return (char*)&buffer_p[bufferTail];

}

int WizFiRingBuffer::availableBufferSize() {
	return bufferSize - 1 - available();
}
