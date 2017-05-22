import xlsxwriter
import random


class Packet(object):
    def __init__(self, ts):
        self.time_slot = ts
        self.next = None


class LinkedList(object):
    head = None
    tail = None
    size = 0

    def add_to_last(self, new_packet):
        if self.head is None:
            self.head = new_packet
            self.tail = new_packet
            self.size += 1
        else:
            new_packet.next = self.head
            self.head = new_packet
            self.size += 1

    def remove_first(self):
        if self.tail is None:
            return
        elif self.head == self.tail:
            self.head = None
            self.tail = None
            self.size -= 1
        else:
            tmp_node = self.head
            while tmp_node.next != self.tail:
                tmp_node = tmp_node.next
            tmp_node.next = None
            self.tail = tmp_node
            self.size -= 1

    def peek(self):
        return self.tail

    def increase_delay(self):
        tmp_node = self.head
        while tmp_node is not None:
            tmp_node.time_slot += 1
            tmp_node = tmp_node.next


class QueueSystem(LinkedList):
    def enqueue(self, new_packet):
        super(QueueSystem, self).add_to_last(new_packet)

    def dequeue(self):
        super(QueueSystem, self).remove_first()


class PacketSimulation(object):
    def __init__(self):
        self.prob_list = []
        p = 0.02
        while p <= 1.0:
            self.prob_list.append(p)
            p += 0.02
        self.throughput_list = []
        self.delay_list = []

    def simulate(self):
        queueSystem = QueueSystem()
        s = 0.8
        p = 0.02
        while p <= 1.0:
            num_service_packets = 0
            total_delay = 0
            while queueSystem.size > 0:
                queueSystem.dequeue()
            for time_slot in range(0, 1000000, 1):
                if queueSystem.size > 0:
                    if random.random() < s:
                        init_time = queueSystem.peek().time_slot
                        total_delay += time_slot - init_time
                        queueSystem.dequeue()
                # queueSystem
                if queueSystem.size < 5:
                    if random.random() < p:
                        queueSystem.enqueue(Packet(time_slot))
                        num_service_packets += 1
            avg_throughput = num_service_packets / float(time_slot)
            avg_delay = total_delay / float(num_service_packets)
            # print(avg_delay)
            self.throughput_list.append(avg_throughput)
            self.delay_list.append(avg_delay)
            p += 0.02


    def create_excel(self):
        workbook = xlsxwriter.Workbook('QueueSimulation.xlsx')
        worksheet = workbook.add_worksheet()
        bold = workbook.add_format({'bold':1})

        # grapgh data
        headings = ['Probability', 'Avg Throughput', 'Avg Delay']
        data = []
        data.append(self.prob_list)
        data.append(self.throughput_list)
        data.append(self.delay_list)

        # add data to excel
        worksheet.write_row('A1', headings, bold)
        worksheet.write_column('A2', data[0])
        worksheet.write_column('B2', data[1])
        worksheet.write_column('C2', data[2])

        # graph 1 (busy vs prob)
        chart1 = workbook.add_chart({'type': 'line'})
        chart1.add_series({
            'name': '=Sheet1!$B$1',
            'categories': '=Sheet1!$A$2:$A$51',
            'values': '=Sheet1!$B$2:$B$51'
        })

        chart1.set_title({'name': 'Avg Throughput vs Probability'})
        chart1.set_x_axis({'name': 'Probability'})
        chart1.set_y_axis({'name': 'Avg Throughput'})
        chart1.set_style(10)

        # graph 2 (busy vs prob)
        chart2 = workbook.add_chart({'type': 'line'})
        chart2.add_series({
            'name': '=Sheet1!$C$1',
            'categories': '=Sheet1!$A$2:$A$51',
            'values': '=Sheet1!$C$2:$C$51'
        })

        chart2.set_title({'name': 'Avg Delay vs Probability'})
        chart2.set_x_axis({'name': 'Probability'})
        chart2.set_y_axis({'name': 'Avg Delay'})
        chart2.set_style(10)

        worksheet.insert_chart('D2', chart1, {'x_offset': 30, 'y_offset': 100})
        worksheet.insert_chart('D2', chart2, {'x_offset': 30, 'y_offset': 500})
        workbook.close()

p = PacketSimulation()
p.simulate()
p.create_excel()
print ("done")
